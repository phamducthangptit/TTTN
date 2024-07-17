package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ManufacturerDTO {
    private String name;
    private String country;
    private String image;
}
