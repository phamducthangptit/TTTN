package com.example.productservice.repository;

import com.example.productservice.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PriceRepository extends JpaRepository<Price, Integer> {
    @Query("SELECT p.id FROM Price p WHERE p.name = :name")
    int getPriceId(@Param("name") String name);
}
