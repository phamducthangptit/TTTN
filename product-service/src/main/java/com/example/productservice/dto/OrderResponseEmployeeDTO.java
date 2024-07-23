package com.example.productservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseEmployeeDTO {
    private static final DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###");
    private int orderId;
    private String totalAmountPaid;
    private String consigneeName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime orderDate;
    private String address;
    private String phone;
    private int checkStatus;

    private List<ProductResponseOrderDTO> listProducts;
    private String formatPrice(BigDecimal price) {
        return decimalFormat.format(price) + " VNĐ";
    }

    // Setter for price
    public void setTotalAmountPaid(BigDecimal price) {
        this.totalAmountPaid = formatPrice(price);
    }
}
