package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequestDTO {
    List<CartRequestOrderDTO> listProducts;
    private String userName;
    private String name;
    private String phone;
    private BigDecimal totalCostOfGoods;
    private BigDecimal shippingFee;
    private String address;
    private String houseNumber;
    private int districtCode;
    private int wardCode;
    private int provinceCode;
}
