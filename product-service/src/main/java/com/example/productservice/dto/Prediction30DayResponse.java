package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prediction30DayResponse {
    private List<Double> top_columns_sum;
    private List<Integer> top_columns_indices;
}
