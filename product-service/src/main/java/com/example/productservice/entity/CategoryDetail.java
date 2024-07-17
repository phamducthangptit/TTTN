package com.example.productservice.entity;

import com.example.productservice.id.CategoryDetailId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "category_detail")
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDetail {
    @EmbeddedId
    private CategoryDetailId categoryDetailId;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    @ManyToOne
    @MapsId("detailId")
    @JoinColumn(name = "detail_id", insertable = false, updatable = false)
    private Detail detail;
}
