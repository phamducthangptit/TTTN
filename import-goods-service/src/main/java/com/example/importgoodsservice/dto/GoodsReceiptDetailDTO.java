package com.example.importgoodsservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoodsReceiptDetailDTO {
    private int productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
