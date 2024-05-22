package com.itc.StockHouse.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "ordered_product")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(OrderedProductKey.class)
public class OrderedProductEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false)
    private OrderEntity order;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false)
    private ProductEntity product;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
