package com.example.productservice.repository;

import com.example.productservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT r FROM Review r WHERE r.orderDetail.seri.product.productId = :productId")
    List<Review> getListReviewByProductId(@Param("productId") int productId);

    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.orderDetail.orderDetailId = :orderDetailId")
    boolean hasReviewed(@Param("orderDetailId") int orderDetailId);
}
