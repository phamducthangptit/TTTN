package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProductDetailRequestDTO {
    private int detailId;
    private String name;
    private String value;
}
