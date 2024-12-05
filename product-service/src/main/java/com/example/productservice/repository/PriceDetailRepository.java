package com.example.productservice.repository;

import com.example.productservice.entity.PriceDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PriceDetailRepository extends JpaRepository<PriceDetail, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM PriceDetail pd WHERE pd.product.productId = :id")
    void deletePriceDetailByProductId(@Param("id") int id);

    @Query("SELECT pd.price.id FROM PriceDetail pd WHERE pd.product.productId = :productId")
    List<Integer> getPriceIdByProductId(@Param("productId") int productId);

    @Query("SELECT pd FROM PriceDetail pd WHERE pd.product.productId = :productId AND pd.price.id = :priceId")
    PriceDetail getPriceDetailByProductIdAndPriceId(@Param("productId") int productId, @Param("priceId") int priceId);

    @Transactional
    @Modifying
    @Query("UPDATE PriceDetail pd SET pd.price1 = :price, pd.startAt = :startAt, pd.endAt = :endAt WHERE pd.product.productId = :productId AND pd.price.id = :priceId")
    void updatePriceDetail(@Param("productId") int productId, @Param("priceId") int priceId, @Param("price") BigDecimal price,
                           @Param("startAt") LocalDateTime startAt, @Param("endAt") LocalDateTime endAt);

    @Transactional
    @Modifying
    @Query("UPDATE PriceDetail pd SET pd.endAt = :endAt WHERE pd.product.productId = :productId AND pd.price.id = :priceId")
    void updateEndDate(@Param("productId") int productId, @Param("priceId") int priceId, @Param("endAt") LocalDateTime endAt);
}
