package com.example.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "seri")
public class Seri {
    @Id
    @Column(name = "seri_number")
    private String seriNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    @OneToMany(mappedBy = "seri")
    private List<OrderDetail> orderDetails;

    @Column(name = "create_at")
    private LocalDateTime createAt;
}
