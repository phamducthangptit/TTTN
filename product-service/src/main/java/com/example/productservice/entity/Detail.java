package com.example.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "detail")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Detail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Integer detailId;
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "detail")
    private List<ProductDetail> productDetails;

    @OneToMany(mappedBy = "detail")
    private List<CategoryDetail> categoryDetails;
}
