package com.example.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "image")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Integer imageId;

    @Column(name = "url")
    private String url;

    @Column(name = "avatar")
    private boolean avatar;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
