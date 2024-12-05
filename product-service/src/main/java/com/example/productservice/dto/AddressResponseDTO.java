package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDTO {
    private int addressId;
    private String houseNumber;
    private int provinceCode;
    private int districtCode;
    private int wardCode;
}
