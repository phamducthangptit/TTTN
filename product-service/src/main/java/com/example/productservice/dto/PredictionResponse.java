package com.example.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PredictionResponse {
    @JsonProperty("top_columns") // Ánh xạ "top_columns" trong JSON
    private List<List<Double>> topColumns;

    @JsonProperty("top_columns_indices") // Ánh xạ "top_columns_indices" trong JSON
    private List<Integer> topColumnsIndices;
}
