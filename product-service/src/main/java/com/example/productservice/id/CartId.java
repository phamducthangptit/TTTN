package com.example.productservice.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CartId implements Serializable {
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "product_id")
    private Integer productId;

    public CartId() {}

    public CartId(Integer userId, Integer productId) {
        this.userId = userId;
        this.productId = productId;
    }

    // hashCode và equals cần được ghi đè để đảm bảo tính duy nhất của khóa chính
    @Override
    public int hashCode() {
        return Objects.hash(userId, productId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartId that = (CartId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(productId, that.productId);
    }
}
