package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailResponseDTO {
    private static final DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###");
    private int productId;
    private int orderDetailId;
    private String image;
    private int stock;
    private int checkStatus;
    private String price;
    private int checkReview;
    private String formatPrice(BigDecimal price) {
        return decimalFormat.format(price) + " VNĐ";
    }

    // Setter for price
    public void setPrice(BigDecimal price) {
        this.price = formatPrice(price);
    }
}
