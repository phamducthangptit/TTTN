package com.example.importgoodsservice.repository;

import com.example.importgoodsservice.entity.WarehouseReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WarehouseReceiptRepository extends JpaRepository<WarehouseReceipt, Integer> {
    @Query("SELECT wr FROM WarehouseReceipt wr WHERE wr.goodsReceipt.id = :goodsReceiptId")
    List<WarehouseReceipt> findByGoodsReceiptId(@Param("goodsReceiptId") Integer goodsReceiptId);

}
