package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {
    private static final DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###");
    private int productId;
    private String name;
    private String image;
    private int quantity;
    private int price;
    private String priceString;
    private String formatPrice(BigDecimal price) {
        return decimalFormat.format(price) + " VNĐ";
    }

    // Setter for price
    public void setPriceString(BigDecimal price) {
        this.priceString = formatPrice(price);
    }
}
