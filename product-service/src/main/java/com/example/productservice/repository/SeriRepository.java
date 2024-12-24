package com.example.productservice.repository;

import com.example.productservice.entity.Seri;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeriRepository extends JpaRepository<Seri, String> {
    @Query("SELECT s FROM Seri s WHERE s.product.productId = :productId")
    List<Seri> getListSeriByProductId(@Param("productId") int productId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Seri s WHERE s.product.productId = :productId")
    void deleteSeriByProductId(@Param("productId") int productId);
}
