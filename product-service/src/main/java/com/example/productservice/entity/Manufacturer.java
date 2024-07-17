package com.example.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Table(name = "manufacturer")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manufacturer_id")
    private int manufacturerId;

    @Column(name = "name")
    private String name;

    @Column(name = "country")
    private String country;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "manufacturer")
    private List<Product> products;
}
