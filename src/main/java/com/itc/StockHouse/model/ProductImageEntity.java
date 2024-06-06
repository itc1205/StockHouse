package com.itc.StockHouse.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "product_image")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageEntity {
    @Id
    @Column(name = "s3_id", nullable = false)
    UUID s3id;

    @Column(name = "product_id", nullable = false)
    UUID productId;
}
