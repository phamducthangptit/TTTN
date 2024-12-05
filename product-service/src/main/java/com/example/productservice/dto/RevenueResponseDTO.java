package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueResponseDTO {
    private BigDecimal revenue;
    List<RevenueDay> listRevenueDay;
}
