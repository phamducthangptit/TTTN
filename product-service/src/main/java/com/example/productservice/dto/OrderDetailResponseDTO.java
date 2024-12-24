package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailResponseDTO {
    private static final DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###");
    private int productId;
    private String productName;
    private int orderDetailId;
    private int orderId;
    private String image;
    private int stock;
    private int checkStatus;
    private String price;
    private int checkReview;
    private String formatPrice(BigDecimal price) {
        return decimalFormat.format(price) + " VNĐ";
    }
    private List<String> listSeri;
    // Setter for price
    public void setPrice(BigDecimal price) {
        this.price = formatPrice(price);
    }
}
