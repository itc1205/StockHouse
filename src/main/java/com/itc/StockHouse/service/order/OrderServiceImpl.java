package com.itc.StockHouse.service.order;

import com.itc.StockHouse.dto.domain.order.OrderDTO;
import com.itc.StockHouse.dto.domain.order.OrderStatusDTO;
import com.itc.StockHouse.dto.domain.order.ProductDTO;
import com.itc.StockHouse.exceptions.AccessDeniedException;
import com.itc.StockHouse.exceptions.ProductNotFoundException;
import com.itc.StockHouse.exceptions.customer.CustomerNotFoundException;
import com.itc.StockHouse.exceptions.order.*;
import com.itc.StockHouse.model.CustomerEntity;
import com.itc.StockHouse.model.OrderEntity;
import com.itc.StockHouse.model.OrderStatus;
import com.itc.StockHouse.model.OrderedProductEntity;
import com.itc.StockHouse.model.ProductEntity;
import com.itc.StockHouse.repository.CustomerRepository;
import com.itc.StockHouse.repository.OrderRepository;
import com.itc.StockHouse.repository.OrderedProductRepository;
import com.itc.StockHouse.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderedProductRepository orderedProductRepository;

    @Override
    public OrderDTO getOrderById(Long customerId, UUID id) {
        OrderEntity order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);

        if (!customerId.equals(order.getCustomer().getId())) {
            throw new AccessDeniedException();
        }

        List<ProductDTO> products = orderedProductRepository.findProjectionByOrderId(id);
        return OrderDTO.builder()
                .orderId(order.getId())
                .products(products)
                .deliveryAddress(order.getDeliveryAddress())
                .totalPrice(products.stream()
                        .map(productDTO -> productDTO.getPrice().multiply(BigDecimal.valueOf(productDTO.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                )
                .build();
    }

    @Override
    @Transactional
    public UUID createOrder(Long customerId, OrderDTO order) {

        CustomerEntity customerEntity = customerRepository.findById(customerId).orElseThrow(
                CustomerNotFoundException::new
        );

        Map<UUID, Integer> requestProducts = order.getProducts().stream().collect(
                Collectors.toMap(
                        ProductDTO::getProductId,
                        ProductDTO::getQuantity,
                        Integer::sum
                )
        );

        OrderEntity newOrder = OrderEntity.builder()
                .deliveryAddress(order.getDeliveryAddress())
                .status(OrderStatus.CREATED)
                .customer(customerEntity)
                .build();

        orderRepository.save(newOrder);

        HashMap<UUID, Integer> insufficientItems = new HashMap<>();
        List<OrderedProductEntity> orderedProductEntities = productRepository.streamAllByIds(requestProducts.keySet()).map(
                productEntity -> {
                    Integer requestQuantity = requestProducts.get(productEntity.getId());

                    if (productEntity.getAmount() < requestQuantity) {
                        insufficientItems.put(productEntity.getId(), productEntity.getAmount());
                    }

                    productEntity.setAmount(productEntity.getAmount() - requestQuantity);

                    return OrderedProductEntity.builder()
                            .price(productEntity.getPrice())
                            .product(productEntity)
                            .order(newOrder)
                            .quantity(requestQuantity)
                            .build();
                })
                .filter(Objects::nonNull)
                .toList();

        if (!insufficientItems.isEmpty()) {
            throw new InsufficientProductsException(insufficientItems);
        }

        if (orderedProductEntities.size() != requestProducts.keySet().size()) {
            throw new ProductNotFoundException("Product not found");
        }


        orderedProductRepository.saveAll(orderedProductEntities);

        return newOrder.getId();
    }

    @Override
    @Transactional
    public void addProductsToOrder(Long customerId, UUID id, List<ProductDTO> products) {

        OrderEntity order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);

        if (!customerId.equals(order.getCustomer().getId())) {
            throw new AccessDeniedException();
        }

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new OrderCantBeChangedException();
        }

        Map<UUID, Integer> requestProducts = products.stream().collect(
                Collectors.toMap(
                        ProductDTO::getProductId,
                        ProductDTO::getQuantity,
                        Integer::sum
                )
        );

        HashMap<UUID, Integer> insufficientItems = new HashMap<>();
        List<OrderedProductEntity> orderedProductEntities = productRepository.streamAllByIds(requestProducts.keySet()).map(
                productEntity -> {
                    Integer requestQuantity = requestProducts.get(productEntity.getId());
                    if (productEntity.getAmount() < requestQuantity) {
                        insufficientItems.put(productEntity.getId(), productEntity.getAmount());
                    }

                    Optional<OrderedProductEntity> orderedProductFromRepo = orderedProductRepository.findByProduct_Id(productEntity.getId());

                    if (orderedProductFromRepo.isPresent()) {
                        OrderedProductEntity existingOrder = orderedProductFromRepo.get();
                        existingOrder.setPrice(productEntity.getPrice());
                        existingOrder.setQuantity(existingOrder.getQuantity() + requestQuantity);
                        orderedProductRepository.save(existingOrder);
                        return null;
                    }

                    productEntity.setAmount(productEntity.getAmount() - requestQuantity);
                    return OrderedProductEntity.builder()
                            .price(productEntity.getPrice())
                            .product(productEntity)
                            .order(order)
                            .quantity(requestQuantity)
                            .build();
                })
                .toList();

        if (!insufficientItems.isEmpty()) {
            throw new InsufficientProductsException(insufficientItems);
        }

        if (orderedProductEntities.size() != requestProducts.keySet().size()) {
            throw new ProductNotFoundException("Product not found");
        }

        orderedProductRepository.saveAll(
                orderedProductEntities.stream()
                        .filter(Objects::nonNull)
                        .toList()
        );
        orderRepository.save(order);
    }


    @Override
    @Transactional
    public void softDeleteOrder(Long customerId, UUID id) {
        OrderEntity order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);

        if (!customerId.equals(order.getCustomer().getId())) {
            throw new AccessDeniedException();
        }

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new OrderCantBeDeletedException();
        }

        for (OrderedProductEntity orderedProduct : order.getProducts()) {
            ProductEntity product = productRepository.findById(orderedProduct.getProduct().getId())
                    // This block should never be executed. In case it does, there is some major inconsistency errors with persistence
                    .orElseThrow(() -> new RuntimeException("This is awfull. Product with id %s does not exist in 'product' table, however it is ordered.".formatted(
                            orderedProduct.getProduct().getId()
                    )));
            product.setAmount(product.getAmount() + orderedProduct.getQuantity());
            productRepository.save(product);
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    public void setStatus(Long customer, OrderStatusDTO orderStatusDTO, UUID orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if (!customer.equals(order.getCustomer().getId())) {
            throw new AccessDeniedException();
        }
        order.setStatus(OrderStatus.valueOf(orderStatusDTO.getCode()));
        orderRepository.save(order);
    }
}
