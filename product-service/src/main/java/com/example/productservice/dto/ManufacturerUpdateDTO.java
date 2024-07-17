package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManufacturerUpdateDTO {
    private int manufacturerId;
    private String name;
    private String country;
    private String image;
}
