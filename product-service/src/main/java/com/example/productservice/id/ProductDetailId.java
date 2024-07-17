package com.example.productservice.id;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductDetailId implements Serializable {
    @Column(name = "product_id")
    @Nationalized
    private int productId;

    @Column(name = "detail_id")
    @Nationalized
    private int detailId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDetailId that = (ProductDetailId) o;
        return productId == that.productId && detailId == that.detailId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, detailId);
    }
}
