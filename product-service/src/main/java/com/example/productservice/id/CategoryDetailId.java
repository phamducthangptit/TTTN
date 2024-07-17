package com.example.productservice.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CategoryDetailId implements Serializable {
    @Column(name = "category_id")
    @Nationalized
    private int categoryId;

    @Column(name = "detail_id")
    @Nationalized
    private int detailId;

    public CategoryDetailId(int detailId, int categoryId) {
    }
    public CategoryDetailId(){}

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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
        CategoryDetailId that = (CategoryDetailId) o;
        return categoryId == that.categoryId && detailId == that.detailId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, detailId);
    }

    @Override
    public String toString() {
        return "CategoryDetailId{" +
                "categoryId=" + categoryId +
                ", detailId=" + detailId +
                '}';
    }
}
