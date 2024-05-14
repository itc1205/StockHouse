package com.itc.StockHouse.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "ordered_product")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class OrderedProductEntity {
    @EmbeddedId
    private OrderedProductKey id;

    @ManyToOne
    @MapsId("orderId")
    private OrderEntity order;

    @ManyToOne
    @MapsId("productId")
    private ProductEntity product;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
