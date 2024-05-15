package com.itc.StockHouse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private List<OrderedProductEntity> products;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;
}
