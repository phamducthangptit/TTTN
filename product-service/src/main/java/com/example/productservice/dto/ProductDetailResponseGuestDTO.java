package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponseGuestDTO {
    private static final DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###");
    private Integer productId;
    private String name;
    private String description;
    private String manufacturerName;
    private String categoryName;
    private BigDecimal price;
    private Integer stock;
    private List<DetailProductResponseDTO> listDetail;
    private List<String> image;
    private List<ReviewResponseGuestDTO> listReview;
    private int countSales;
}
