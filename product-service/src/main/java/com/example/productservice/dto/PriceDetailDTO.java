package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceDetailDTO {
    private int priceId;
    private int productId;
    private BigDecimal price;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
