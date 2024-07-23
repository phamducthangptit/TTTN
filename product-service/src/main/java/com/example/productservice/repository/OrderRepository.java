package com.example.productservice.repository;

import com.example.productservice.entity.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByUser_userId(int userId);

    @Query("SELECT o FROM Order o WHERE o.status = :type")
    List<Order> getAllOrderByType(@Param("type") String type);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.status = :status WHERE o.orderId = :orderId")
    void updateStatusOrder(@Param("orderId") int orderId, @Param("status") String status);
}
