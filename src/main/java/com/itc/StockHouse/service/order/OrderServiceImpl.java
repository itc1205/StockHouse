package com.itc.StockHouse.service.order;

import com.itc.StockHouse.dto.domain.customer.CustomerDTO;
import com.itc.StockHouse.dto.domain.order.OrderDTO;
import com.itc.StockHouse.dto.domain.order.OrderStatusDTO;
import com.itc.StockHouse.dto.domain.order.ProductDTO;
import com.itc.StockHouse.exceptions.InsufficientRightsException;
import com.itc.StockHouse.exceptions.ProductNotFoundException;
import com.itc.StockHouse.exceptions.customer.CustomerNotFoundException;
import com.itc.StockHouse.exceptions.order.*;
import com.itc.StockHouse.model.CustomerEntity;
import com.itc.StockHouse.model.OrderEntity;
import com.itc.StockHouse.model.OrderStatus;
import com.itc.StockHouse.model.OrderedProductEntity;
import com.itc.StockHouse.model.OrderedProductKey;
import com.itc.StockHouse.model.ProductEntity;
import com.itc.StockHouse.repository.CustomerRepository;
import com.itc.StockHouse.repository.OrderRepository;
import com.itc.StockHouse.repository.OrderedProductRepository;
import com.itc.StockHouse.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderedProductRepository orderedProductRepository;

    @Override
    public OrderDTO getOrderById(CustomerDTO customer, UUID id) throws OrderNotFoundException, InsufficientRightsException {
        OrderEntity order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);

        if (!customer.getId().equals( order.getCustomer().getId())) {
            throw new InsufficientRightsException();
        }

        List<ProductDTO> products = orderedProductRepository.findByOrderId(id);
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
    public UUID createOrder(CustomerDTO customer, OrderDTO order) throws InsufficientProductsException, ProductNotFoundException, CustomerNotFoundException {

        CustomerEntity customerEntity = customerRepository.findById(customer.getId()).orElseThrow(
                CustomerNotFoundException::new
        );



        OrderEntity newOrder = OrderEntity.builder()
                .deliveryAddress(order.getDeliveryAddress())
                .status(OrderStatus.CREATED)
                .customer(customerEntity)
                .build();
        newOrder = orderRepository.save(newOrder);

        Map<UUID, Integer> requestProducts = order.getProducts().stream().collect(
                Collectors.toMap(
                        ProductDTO::getProductId,
                        ProductDTO::getQuantity,
                        Integer::sum
                )
        );


        HashMap<UUID, Integer> insufficientItems = new HashMap<>();
        OrderEntity finalNewOrder = newOrder;
        List<OrderedProductEntity> orderedProductEntities = productRepository.findAllById(requestProducts.keySet()).stream().map(
                productEntity -> {
                    if (productEntity.getAmount() < requestProducts.get(productEntity.getId())) {
                        insufficientItems.put(productEntity.getId(), productEntity.getAmount());
                    }
                    productEntity.setAmount(productEntity.getAmount() - requestProducts.get(productEntity.getId()));
                    return OrderedProductEntity.builder()
                            .id(new OrderedProductKey(finalNewOrder.getId(), productEntity.getId()))
                            .product(productEntity)
                            .price(productEntity.getPrice())
                            .quantity(requestProducts.get(productEntity.getId()))
                            .order(finalNewOrder)
                            .build();
                }
        ).toList();

        if (orderedProductEntities.size() != requestProducts.keySet().size()){
            throw new ProductNotFoundException("Product not found");
        }
        if (!insufficientItems.isEmpty()) {
            throw new InsufficientProductsException(insufficientItems);
        }
        orderedProductRepository.saveAll(orderedProductEntities);
        return newOrder.getId();
    }

    @Override
    @Transactional
    public void addProductsToOrder(CustomerDTO customer, UUID id, List<ProductDTO> products) throws InsufficientProductsException, OrderNotFoundException, InsufficientRightsException, OrderCantBeChangedException {

        OrderEntity order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);


        if (!customer.getId().equals( order.getCustomer().getId())) {
            throw new InsufficientRightsException();
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
                    if (productEntity.getAmount() < requestProducts.get(productEntity.getId())) {
                        insufficientItems.put(productEntity.getId(), productEntity.getAmount());
                    }
                    productEntity.setAmount(productEntity.getAmount() - requestProducts.get(productEntity.getId()));
                    return OrderedProductEntity.builder()
                            .id(new OrderedProductKey(order.getId(), productEntity.getId()))
                            .product(productEntity)
                            .price(productEntity.getPrice())
                            .quantity(requestProducts.get(productEntity.getId()))
                            .order(order)
                            .build();
                }
        ).toList();

        if (orderedProductEntities.size() != requestProducts.keySet().size()){
            throw new ProductNotFoundException("Product not found");
        }
        if (!insufficientItems.isEmpty()) {
            throw new InsufficientProductsException(insufficientItems);
        }
        orderedProductRepository.saveAll(orderedProductEntities);
    }



    @Override
    @Transactional
    public void softDeleteOrder(CustomerDTO customer, UUID id) throws OrderCantBeDeletedException, OrderNotFoundException, InsufficientRightsException {
        OrderEntity order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);

        if (!customer.getId().equals( order.getCustomer().getId())) {
            throw new InsufficientRightsException();
        }

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new OrderCantBeDeletedException();
        }

        for (OrderedProductEntity orderedProduct: order.getProducts()) {
            // This block should never be executed. In case it does, there is some major inconsistency errors with persistence
            ProductEntity product = productRepository.findById(orderedProduct.getId().getProductId())
                    .orElseThrow(() -> new RuntimeException("This is awfull. Product with id %s does not exist in 'product' table, however it is ordered.".formatted(
                            orderedProduct.getId()
                    )));
            product.setAmount(product.getAmount() + orderedProduct.getQuantity());
            productRepository.save(product);
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    public void setStatus(CustomerDTO customer, OrderStatusDTO orderStatusDTO, UUID id) throws OrderNotFoundException, InsufficientRightsException {
        OrderEntity order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);

        if (!customer.getId().equals(order.getCustomer().getId())) {
            throw new InsufficientRightsException();
        }
        order.setStatus(OrderStatus.valueOf(orderStatusDTO.getCode()));
        orderRepository.save(order);
    }
}
