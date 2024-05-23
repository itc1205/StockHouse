package com.itc.StockHouse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class OrderedProductKey implements Serializable {
    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "order_id")
    private UUID orderId;
}
