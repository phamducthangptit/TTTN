package com.example.productservice.repository;

import com.example.productservice.entity.Image;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Image i WHERE i.product.productId = :productId")
    void deleteImageByProductId(@Param("productId") int productId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Image i WHERE i.product.productId = :productId AND i.url = :url")
    void deleteImageByProductIdAndUrl(@Param("productId") int productId, @Param("url") String url);

    @Query("SELECT COUNT(i.product.productId) FROM Image i WHERE i.product.productId = :productId")
    int countImageProduct(@Param("productId") int productId);
}
