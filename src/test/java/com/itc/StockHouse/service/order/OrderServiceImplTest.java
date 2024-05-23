package com.itc.StockHouse.service.order;

import com.itc.StockHouse.dto.domain.order.OrderDTO;
import com.itc.StockHouse.dto.domain.order.ProductDTO;
import com.itc.StockHouse.model.CustomerEntity;
import com.itc.StockHouse.model.OrderEntity;
import com.itc.StockHouse.model.OrderedProductEntity;
import com.itc.StockHouse.model.ProductEntity;
import com.itc.StockHouse.motherobject.MotherObject;
import com.itc.StockHouse.repository.CustomerRepository;
import com.itc.StockHouse.repository.OrderRepository;
import com.itc.StockHouse.repository.OrderedProductRepository;
import com.itc.StockHouse.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Slf4j
class OrderServiceImplTest {

    private final ProductEntity productWith100PriceAndAmountOf50 = MotherObject
            .aStockWithRandomVendorCode()
            .withPrice(BigDecimal.valueOf(100))
            .withAmount(50)
            .build();

    private final ProductEntity productWith200PriceAndAmountOf100 = MotherObject
            .aStockWithRandomVendorCode()
            .withPrice(BigDecimal.valueOf(200))
            .withAmount(100)
            .build();

    private final ProductEntity productWit10PriceAndAmountOf10 = MotherObject
            .aStockWithRandomVendorCode()
            .withPrice(BigDecimal.TEN)
            .withAmount(10)
            .build();

    private final CustomerEntity customer = MotherObject
            .aCustomerWithRandomLogin()
            .build();

    private final CustomerEntity anotherCustomer = MotherObject
            .aCustomerWithRandomLogin()
            .build();

    private final OrderedProductEntity orderedProductWith200PriceAndAmountOf10 = OrderedProductEntity.builder()
            .product(productWith100PriceAndAmountOf50)
            .price(productWith100PriceAndAmountOf50.getPrice())
            .quantity(10)
            .build();

    private final OrderEntity order = MotherObject.aDefaultOrder()
            .withCustomer(customer)
            .build();

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderedProductRepository orderedProductRepository;

    private OrderService orderService;


    @BeforeEach
    void setUp() {
        log.info("Initialising test data");
        productRepository.saveAll(
                Arrays.asList(
                        productWith100PriceAndAmountOf50,
                        productWith200PriceAndAmountOf100,
                        productWit10PriceAndAmountOf10
                )
        );

        customerRepository.saveAll(
                Arrays.asList(
                        customer,
                        anotherCustomer
                )
        );
        orderRepository.save(order);

        orderedProductWith200PriceAndAmountOf10.setOrder(order);
        orderedProductRepository.save(orderedProductWith200PriceAndAmountOf10);
        log.info("Test data initialised");
        orderService = new OrderServiceImpl(orderRepository, productRepository, customerRepository, orderedProductRepository);
    }

    @AfterEach
    void tearDown() {
        log.info("Finished test, cleaning up data");
        // Cleanup repos
        orderedProductRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();
        log.info("Data cleanup is complete");
    }

    @Test
    void givenNewOrder_whenPlacingNewOrder_thenNewOrderPlaced() {
        OrderDTO orderDTO = OrderDTO.builder()
                .deliveryAddress("Something new")
                .products(
                        new ArrayList<>(List.of(
                                ProductDTO.builder()
                                        .productId(productWith200PriceAndAmountOf100.getId())
                                        .quantity(20)
                                        .build(),
                                ProductDTO.builder()
                                        .productId(productWith100PriceAndAmountOf50.getId())
                                        .quantity(20)
                                        .build()
                        ))
                )
                .build();
        UUID newOrderId = orderService.createOrder(customer.getId(), orderDTO);

        OrderDTO newOrder = orderService.getOrderById(customer.getId(), newOrderId);
        assertEquals(newOrder.getOrderId(), newOrderId);
        assertEquals(newOrder.getProducts().size(), orderDTO.getProducts().size());
        assertEquals(newOrder.getDeliveryAddress(), orderDTO.getDeliveryAddress());

        BigDecimal firstProductPrice = new BigDecimal("100.00").multiply(BigDecimal.valueOf(20));
        BigDecimal secondProductPrice = new BigDecimal("200.00").multiply(BigDecimal.valueOf(20));
        BigDecimal totalPriceExpecting = firstProductPrice.add(secondProductPrice);

        assertEquals(newOrder.getTotalPrice(), totalPriceExpecting);
    }

    @Test
    void givenExistingOrderWithOneUniqueProduct_whenAddingTwoDifferentProducts_thenOrderServiceReturnsThreeOrderedProducts() {
        orderService.addProductsToOrder(
                order.getCustomer().getId(),
                order.getId(),
                Arrays.asList(
                        ProductDTO.builder()
                                .productId(productWith200PriceAndAmountOf100.getId())
                                .quantity(10)
                                .build(),
                        ProductDTO.builder()
                                .productId(productWit10PriceAndAmountOf10.getId())
                                .quantity(5)
                                .build()
                )
        );

        OrderDTO orderFromService = orderService.getOrderById(order.getCustomer().getId(), order.getId());

        assertEquals(3, orderFromService.getProducts().size());
    }

    @Test
    void givenExistingOrderWithOneUniqueProduct_whenAddingSameProduct_thenOrderServiceAppendsAmount() {
        Integer currentQuantity = orderService.getOrderById(customer.getId(), order.getId())
                .getProducts()
                .get(0)
                .getQuantity();

        Integer appendQuantity = 10;
        Integer wantedQuantity = currentQuantity + appendQuantity;

        orderService.addProductsToOrder(
                order.getCustomer().getId(),
                order.getId(),
                Arrays.asList(
                        // Add same product
                        ProductDTO.builder()
                                .productId(productWith100PriceAndAmountOf50.getId())
                                .quantity(10)
                                .build()
                )
        );

        OrderDTO orderFromService = orderService.getOrderById(order.getCustomer().getId(), order.getId());

        assertEquals(1, orderFromService.getProducts().size());
        assertEquals(productWith100PriceAndAmountOf50.getId(), orderFromService.getProducts().get(0).getProductId());
        assertEquals(wantedQuantity, orderFromService.getProducts().get(0).getQuantity());
    }
}