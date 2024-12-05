package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressRequestDTO {
    private int userId;
    private String houseNumber;
    private int provinceCode;
    private int districtCode;
    private int wardCode;
}
