package com.example.productservice.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "address", schema = "sell_electronics_online")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Integer id;

    @Column(name = "house_number", length = 45)
    private String houseNumber;

    @Column(name = "wards")
    private int wards;

    @Column(name = "district")
    private int district;

    @Column(name = "province")
    private int province;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}