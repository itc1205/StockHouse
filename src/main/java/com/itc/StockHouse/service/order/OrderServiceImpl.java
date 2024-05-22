package com.itc.StockHouse.service.order;

import com.itc.StockHouse.dto.domain.order.OrderDTO;
import com.itc.StockHouse.dto.domain.order.OrderStatusDTO;
import com.itc.StockHouse.dto.domain.order.ProductDTO;
import com.itc.StockHouse.exceptions.AccessDeniedException;
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
import java.util.function.Function;
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
    public UUID createOrder(Long customerId, OrderDTO createOrderRequest) {
        // 1. Находим клиента
        CustomerEntity customerEntity = customerRepository.findById(customerId).orElseThrow(
                CustomerNotFoundException::new
        );

        // 2. Создаем заказ
        OrderEntity newOrder = OrderEntity.builder()
                .deliveryAddress(createOrderRequest.getDeliveryAddress())
                .status(OrderStatus.CREATED)
                .customer(customerEntity)
                .build();


        // 3. Мапим продукты в мапу UUID-Integer
        Map<UUID, Integer> requestProductQuantity = createOrderRequest.getProducts().stream().collect(
                Collectors.toMap(
                        ProductDTO::getProductId,
                        ProductDTO::getQuantity,
                        Integer::sum
                )
        );

        // 4. Делаем один запрос в бд для нахождения необходимых товаров
        List<ProductEntity> productEntities = productRepository.findAllById(requestProductQuantity.keySet());


        // 5. Проверяем, есть ли все товары в базе данных
        if (requestProductQuantity.keySet().size() != productEntities.size()) {

            Set<UUID> foundIds = productEntities.stream()
                    .map(ProductEntity::getId)
                    .collect(Collectors.toSet());

            Set<UUID> missingIds = requestProductQuantity.keySet().stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toSet());

            throw new ProductsNotFoundException(missingIds);
        }


        // 6. Создаем заказ на продукты
        List<OrderedProductEntity> orderedProductEntities = productEntities.stream()
                .map(
                        productEntity -> {
                            Integer requestQuantity = requestProductQuantity.get(productEntity.getId());

                            if (!productEntity.getIsAvailable()) {
                                throw new ProductIsNotAvailableException(productEntity.getId());
                            }

                            if (productEntity.getAmount() < requestQuantity) {
                                throw new InsufficientProductsException(productEntity.getId(), productEntity.getAmount());
                            }

                            productEntity.setAmount(productEntity.getAmount() - requestQuantity);

                            return OrderedProductEntity.builder()
                                    .price(productEntity.getPrice())
                                    .product(productEntity)
                                    .order(newOrder)
                                    .quantity(requestQuantity)
                                    .build();
                        })
                .collect(Collectors.toCollection(LinkedList::new));

        newOrder.setProducts(orderedProductEntities);
        return orderRepository.save(newOrder).getId();
    }

    @Override
    @Transactional
    public void addProductsToOrder(Long customerId, UUID orderId, List<ProductDTO> products) {

        // 1. Находим заказ
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        // 2. Проверяем, а тот ли клиент
        if (!customerId.equals(order.getCustomer().getId())) {
            throw new AccessDeniedException();
        }

        // 3. Проверяем статус заказа
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new OrderCantBeChangedException();
        }

        // 3. Мапим продукты в мапу UUID-Integer
        Map<UUID, Integer> requestProductQuantity = products.stream().collect(
                Collectors.toMap(
                        ProductDTO::getProductId,
                        ProductDTO::getQuantity,
                        Integer::sum
                )
        );

        // 4. Делаем один запрос в бд для нахождения необходимых товаров
        List<ProductEntity> productEntities = productRepository.findAllById(requestProductQuantity.keySet());


        // 5. Проверяем, есть ли все товары в базе данных
        if (requestProductQuantity.keySet().size() != productEntities.size()) {

            Set<UUID> foundIds = productEntities.stream()
                    .map(ProductEntity::getId)
                    .collect(Collectors.toSet());

            Set<UUID> missingIds = requestProductQuantity.keySet().stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toSet());

            throw new ProductsNotFoundException(missingIds);
        }

        // 6. Получаем все товары, которые можно обновить
        Map<UUID, OrderedProductEntity> alreadyOrderedProducts = order.getProducts()
                .stream()
                .collect(Collectors.toMap(orderedProduct -> orderedProduct.getProduct().getId(), Function.identity()));

        List<OrderedProductEntity> orderedProductEntities = productRepository.streamAllByIds(requestProductQuantity.keySet()).map(
                        productEntity -> {
                            Integer requestQuantity = requestProductQuantity.get(productEntity.getId());

                            if (productEntity.getAmount() < requestQuantity) {
                                throw new InsufficientProductsException(productEntity.getId(), productEntity.getAmount());
                            }

                            if (!productEntity.getIsAvailable()) {
                                throw new ProductIsNotAvailableException(productEntity.getId());
                            }

                            Optional<OrderedProductEntity> alreadyOrderedProduct = Optional.ofNullable(
                                    alreadyOrderedProducts.getOrDefault(productEntity.getId(), null)
                            );

                            if (alreadyOrderedProduct.isPresent()) {
                                OrderedProductEntity existingOrder = alreadyOrderedProduct.get();
                                existingOrder.setPrice(productEntity.getPrice());
                                existingOrder.setQuantity(existingOrder.getQuantity() + requestQuantity);
                                return existingOrder;
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

        order.getProducts().addAll(orderedProductEntities);
        orderRepository.save(order);
    }


    @Override
    @Transactional
    public void softDeleteOrder(Long customerId, UUID orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if (!customerId.equals(order.getCustomer().getId())) {
            throw new AccessDeniedException();
        }

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new OrderCantBeDeletedException();
        }

        for (OrderedProductEntity orderedProduct : order.getProducts()) {
            ProductEntity product = productRepository.findById(orderedProduct.getProduct().getId())
                    // This block should never be executed. In case it does, there is some major inconsistency errors with persistence
                    .orElseThrow(() -> new RuntimeException("This is awful. Product with id %s does not exist in 'productId' table, however it is ordered.".formatted(
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
