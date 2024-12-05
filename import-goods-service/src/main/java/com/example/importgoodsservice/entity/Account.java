package com.example.importgoodsservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
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