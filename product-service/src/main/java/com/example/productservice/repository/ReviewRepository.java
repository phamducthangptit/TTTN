package com.example.productservice.repository;

import com.example.productservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByUser_userId(int userId);
    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.user.userId = :userId AND r.product.productId = :productId AND r.order.orderId = :orderId")
    boolean existsByUserIdAndProductIdAndOrderId(@Param("userId") int userId, @Param("productId") int productId, @Param("orderId") int orderId);

}
