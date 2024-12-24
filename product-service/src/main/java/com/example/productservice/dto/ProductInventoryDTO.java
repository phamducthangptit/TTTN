package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductInventoryDTO {
    private int productId;
    private String productName;
    private int quantityProductNew;
    private int quantityProductOld;
    private int quantityProductSales;
}
