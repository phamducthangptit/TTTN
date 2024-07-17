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
public class ProductResponseDTO {
    private static final DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###");
    private Integer productId;
    private String name;
    private String description;
    private String manufacturerName;
    private String price;
    private Integer stock;
    private String image;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime createAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime updateAt;

    private String formatPrice(BigDecimal price) {
        return decimalFormat.format(price) + " VNĐ";
    }

    // Setter for price
    public void setPrice(BigDecimal price) {
        this.price = formatPrice(price);
    }
}
