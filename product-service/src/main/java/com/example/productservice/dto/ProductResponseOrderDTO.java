package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProductResponseOrderDTO {
    private static final DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###");
    private int productId;
    private String name;
    private String price;
    private String image;
    private int quantity;

    private String formatPrice(BigDecimal price) {
        return decimalFormat.format(price) + " VNĐ";
    }

    // Setter for price
    public void setPrice(BigDecimal price) {
        this.price = formatPrice(price);
    }
}
