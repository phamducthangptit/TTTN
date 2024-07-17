package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {
    private String name;
    private String price;
    private String stock;
    private String description;
    private List<String> images;
    private List<ProductDetailRequestDTO> productDetails;
    private int categoryId;
    private int manufacturerId;
}
