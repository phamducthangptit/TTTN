package com.example.importgoodsservice.repository;

import com.example.importgoodsservice.entity.GoodsReceipt;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoodsReceiptRepository extends JpaRepository<GoodsReceipt, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM GoodsReceipt g WHERE g.id = :goodsReceiptId")
    void deleteById(@Param("goodsReceiptId") int goodsReceiptId);

    GoodsReceipt findByid(int goodsReceiptId);


    @Query("SELECT g FROM GoodsReceipt g WHERE g.isComplete = :isComplete ORDER BY g.createAt DESC")
    List<GoodsReceipt> getAllByIsComplete(@Param("isComplete") boolean isComplete);

    @Transactional
    @Modifying
    @Query("UPDATE GoodsReceipt SET isComplete = true WHERE id = :id")
    void updateIsComplete(@Param("id") int id);
}
