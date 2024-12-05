package com.example.importgoodsservice.repository;

import com.example.importgoodsservice.entity.GoodsReceiptDetail;
import com.example.importgoodsservice.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoodsReceiptDetailRepository extends JpaRepository<GoodsReceiptDetail, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM GoodsReceiptDetail g WHERE g.goodsReceipt.id = :goodsReceiptId")
    void deleteByGoodsReceiptId(@Param("goodsReceiptId") int goodsReceiptId);

    @Query("SELECT g.product FROM GoodsReceiptDetail g WHERE g.goodsReceipt.id = :id AND  g.product.id NOT IN (SELECT wcd.product.id FROM WarehouseReceiptDetail wcd INNER JOIN WarehouseReceipt wr ON wcd.warehouseReceipt.id = wr.id  AND wr.goodsReceipt.id = :id)")
    List<Product> getAllProductNoWarehouseReceipt(@Param("id") int id);
}
