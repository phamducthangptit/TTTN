package com.example.importgoodsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WarehouseReceiptDetailDTO {
    private int goodsReceiptId;
    private int productId;
    private int receivedQuantity;
    private BigDecimal receivedPrice;
}
