package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Predict7DaysResponseDTO {
    private int day;
    private List<ProductPredictResponse> predict;
}
