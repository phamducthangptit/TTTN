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
public class ProductResponseDTO {
    private static final DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###");
    private Integer productId;
    private String name;
    private String description;
    private String manufacturerName;
    private String categoryName;
    private String price;
    private Integer stock;
    private List<String> image;
    private int countSales;
    private BigDecimal priceSale;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime createAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime updateAt;

    private int isPresent;

    private String formatPrice(BigDecimal price) {
        return decimalFormat.format(price) + " VNĐ";
    }

    // Setter for price
    public void setPrice(BigDecimal price) {
        this.price = formatPrice(price);
    }
}
