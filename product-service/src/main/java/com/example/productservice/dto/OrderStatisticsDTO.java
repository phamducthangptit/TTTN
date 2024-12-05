package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderStatisticsDTO {
    private int completedOrders;
    private int newOrders;
    private int cancelledOrders;
}
