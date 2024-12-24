package com.example.productservice.repository;

import com.example.productservice.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId ")
    List<Product> getAllProductByCategoryId(@Param("categoryId") int categoryId);


    @Query("SELECT p FROM Product p WHERE p.isPresent = 1")
    List<Product> getAllProductByIsPresent();

    @Query("SELECT p FROM Product p WHERE (p.isPresent = 1) AND (p.category.name LIKE %:query% OR p.name LIKE %:query%)")
    List<Product> getProductByQuery(@Param("query") String query);


    Optional<Product> findByproductId(int id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Product p WHERE p.productId = :productId")
    void deleteProductById(@Param("productId") int productId);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.name = :name, p.description = :description, p.category.categoryId = :categoryId, " +
            "p.manufacturer.manufacturerId = :manufacturerId WHERE p.productId = :productId")
    void updateProduct(@Param("name") String name,
                       @Param("description") String description, @Param("categoryId") int categoryId,
                       @Param("manufacturerId") int manufacturerId, @Param("productId") int productId);

    @Query("SELECT p.stock FROM Product p WHERE p.productId = :productId")
    int getStockProduct(@Param("productId") int productId);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.stock = p.stock - :quantity WHERE p.productId = :productId")
    void updateStockProduct(@Param("quantity") int quantity, @Param("productId") int productId);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.isPresent = :isPresent WHERE p.productId = :productId")
    void updateIsPresent(@Param("isPresent") int isPresent, @Param("productId") int productId);

    @Query("SELECT p.name FROM Product p WHERE p.productId = :productId")
    String getProductNameByProductId(@Param("productId") int productId);
}
