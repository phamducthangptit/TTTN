package com.example.importgoodsservice.repository;

import com.example.importgoodsservice.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p WHERE p.manufacturer.id = :manufacturerId")
    List<Product> getListProductByManufacturerId(@Param("manufacturerId") int manufacturerId);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.stock = p.stock + :stock WHERE p.id = :id")
    void updateStock(@Param("id") int id, @Param("stock") int stock);
}
