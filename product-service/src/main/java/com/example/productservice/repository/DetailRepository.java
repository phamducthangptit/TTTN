package com.example.productservice.repository;

import com.example.productservice.entity.Detail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DetailRepository extends JpaRepository<Detail, Integer> {
    Optional<Detail> findByname(String name);

    Optional<Detail> findBydetailId(int id);
}
