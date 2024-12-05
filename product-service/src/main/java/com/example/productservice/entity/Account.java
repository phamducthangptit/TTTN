package com.example.productservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account", schema = "sell_electronics_online")
public class Account {
    @Id
    @Column(name = "account_id", nullable = false)
    private Integer id;

    @Column(name = "user_name", nullable = false, length = 45)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "create_at")
    private LocalDateTime createAt;
}
