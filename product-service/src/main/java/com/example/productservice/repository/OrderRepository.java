package com.example.productservice.repository;

import com.example.productservice.entity.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order findByorderId(int orderId);
    @Query("SELECT o FROM Order o WHERE o.user.userId = :userId ORDER BY o.orderDate DESC")
    List<Order> findAllByUserIdOrderByOrderDateDesc(@Param("userId") int userId);

    @Query("SELECT o FROM Order o WHERE o.status = :type ORDER BY o.orderDate DESC")
    List<Order> getAllOrderByType(@Param("type") String type);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.status = :status WHERE o.orderId = :orderId")
    void updateStatusOrder(@Param("orderId") int orderId, @Param("status") String status);

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.statusPayment = :statusPayment WHERE o.orderId = :orderId")
    void updateStatusPayment(@Param("orderId") int orderId, @Param("statusPayment") int statusPayment);

    @Query("SELECT o.orderDate, SUM(o.totalAmount) " +
            "FROM Order o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate AND o.statusPayment = 1" +
            "GROUP BY o.orderDate " +
            "ORDER BY o.orderDate")
    List<Object[]> getRevenueForDateRange(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    @Query("SELECT EXTRACT(YEAR FROM o.orderDate) AS year, " +
            "       EXTRACT(WEEK FROM o.orderDate) AS week, " +
            "       SUM(o.totalAmount) " +
            "FROM Order o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate AND o.statusPayment = 1" +
            "GROUP BY EXTRACT(YEAR FROM o.orderDate), EXTRACT(WEEK FROM o.orderDate) " +
            "ORDER BY year, week")
    List<Object[]> getRevenueForWeekRange(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    @Query("SELECT EXTRACT(YEAR FROM o.orderDate) AS year, " +
            "       EXTRACT(MONTH FROM o.orderDate) AS month, " +
            "       SUM(o.totalAmount) " +
            "FROM Order o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate AND o.statusPayment = 1" +
            "GROUP BY EXTRACT(YEAR FROM o.orderDate), EXTRACT(MONTH FROM o.orderDate) " +
            "ORDER BY year, month")
    List<Object[]> getRevenueForMonthRange(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT DATE(o.orderDate) AS orderDate, o.status, COUNT(o) " +
            "FROM Order o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(o.orderDate), o.status " +
            "ORDER BY DATE(o.orderDate)")
    List<Object[]> getOrderCountByStatusForDateRange(@Param("startDate") LocalDateTime startDate,
                                                     @Param("endDate") LocalDateTime endDate);



}
