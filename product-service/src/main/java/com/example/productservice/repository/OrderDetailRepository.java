package com.example.productservice.repository;

import com.example.productservice.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    @Query("SELECT COUNT(od) FROM OrderDetail od WHERE od.product.productId = :productId")
    int getQuantityProductInOrder(@Param("productId") int productId);

    List<OrderDetail> findAllByOrder_orderId(int orderId);
}
