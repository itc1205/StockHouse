package com.itc.StockHouse.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OrderedProductKey implements Serializable {
    private ProductEntity product;
    private OrderEntity order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderedProductKey that)) return false;
        return Objects.equals(product, that.product) && Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, order);
    }
}
