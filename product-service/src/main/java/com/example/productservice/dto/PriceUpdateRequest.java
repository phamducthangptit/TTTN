package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceUpdateRequest {
    private int productId;
    private String price;
    private int priceIdNew;
    private int priceIdOld;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public BigDecimal getPriceAsBigDecimal() {
        if (price != null && !price.isEmpty()) {
            try {
                return new BigDecimal(price);
            } catch (NumberFormatException e) {
                System.out.println("Invalid price format: " + price);
            }
        }
        return BigDecimal.ZERO;
    }
}
