package com.example.productservice.repository;

import com.example.productservice.entity.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByname(String name);

    Category findBycategoryId(int id);

    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.name = :name WHERE c.id = :id")
    public void updateCategory(@Param("name") String name, @Param("id") int id);
}
