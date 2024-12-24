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

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.paymentDate = :paymentDate WHERE o.orderId = :orderId")
    void updatePaymentDate(@Param("orderId") int orderId, @Param("paymentDate") LocalDateTime paymentDate);

    @Query("SELECT " +
            "COALESCE(SUM(CASE WHEN o.status = 'Hoàn thành' THEN 1 ELSE 0 END), 0) AS completedOrders, " +
            "COALESCE(SUM(CASE WHEN o.status = 'Mới' THEN 1 ELSE 0 END), 0) AS newOrders, " +
            "COALESCE(SUM(CASE WHEN o.status = 'Hủy' THEN 1 ELSE 0 END), 0) AS canceledOrders " +
            "FROM Order o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Object[]> getOrderStatistics(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


    @Query("SELECT " +
            "CAST(o.paymentDate AS LocalDate) AS paymentDFate, " +
            "SUM(o.totalCostOfGoods) AS dailyRevenue " +
            "FROM Order o " +
            "WHERE o.statusPayment = 1 " +
            "AND o.paymentDate BETWEEN :startDate AND :endDate " +
            "GROUP BY CAST(o.paymentDate AS LocalDate) " +
            "ORDER BY CAST(o.paymentDate AS LocalDate)")
    List<Object[]> getDailyCompletedOrderStatistics(@Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o FROM Order o WHERE o.orderDate >= :daysAgo AND o.status != 'Hủy' ORDER BY o.orderDate ASC")
    List<Order> getAllOrderInPrevDays(@Param("daysAgo") LocalDateTime daysAgo);


}
