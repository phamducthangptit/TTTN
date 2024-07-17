package com.example.productservice.entity;

import com.example.productservice.id.ProductDetailId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "product_detail")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetail {
    @EmbeddedId
    private ProductDetailId productDetailId;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "detail_id", insertable = false, updatable = false)
    private Detail detail;
}
