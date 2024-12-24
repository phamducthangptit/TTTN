package com.example.productservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private static final DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###");
    private int orderId;
    private String totalAmountPaid;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime orderDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime paymentDate;
    private String address;
    private String status;
    private String phone;
    private int totalProduct;
    private int checkStatus;
    private int statusPayment;
    private String shippingFee;

    private String formatPrice(BigDecimal price) {
        return decimalFormat.format(price) + " VNĐ";
    }

    // Setter for price
    public void setTotalAmountPaid(BigDecimal price) {
        this.totalAmountPaid = formatPrice(price);
    }

    // Setter for price
    public void setTotalShippingFee(BigDecimal price) {
        this.shippingFee = formatPrice(price);
    }
}
