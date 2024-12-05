package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {
    private String name;
    private String stock;
    private String price;
    private String description;
    private List<String> images;
    private List<ProductDetailRequestDTO> productDetails;
    private int categoryId;
    private int manufacturerId;
    private int weight;
    private int height;
    private int width;
    private int length;
    private String namePrice;
    private LocalDateTime startDatePrice;
    private LocalDateTime endDatePrice;
    private int isPresent;
}
