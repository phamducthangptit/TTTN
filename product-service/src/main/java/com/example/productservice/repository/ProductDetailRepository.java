package com.example.productservice.repository;

import com.example.productservice.entity.ProductDetail;
import com.example.productservice.id.ProductDetailId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, ProductDetailId> {

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductDetail pd WHERE pd.productDetailId.productId = :productId")
    void deleteProductDetailByProductId(@Param("productId") int productId);

    @Query("SELECT COUNT(pd) FROM ProductDetail pd WHERE pd.productDetailId.productId = :productId")
    int countProductDetailByProductId(@Param("productId") int productId);

    @Modifying
    @Transactional
    @Query("UPDATE ProductDetail pd SET pd.value = :value WHERE pd.productDetailId.productId = :productId AND pd.productDetailId.detailId = :detailId")
    void updateProductDetailByProductIdAndDetailId(@Param("value") String value, @Param("productId") int productId, @Param("detailId") int detailId);
}
