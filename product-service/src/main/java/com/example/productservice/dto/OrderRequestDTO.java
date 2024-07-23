package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequestDTO {
    List<CartRequestOrderDTO> listProducts;
    private String userName;
    private String name;
    private String address;
    private String phone;
    private int totalAmount;
}
