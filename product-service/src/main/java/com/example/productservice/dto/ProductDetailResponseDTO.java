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
public class ProductDetailResponseDTO {
    private static final DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###");
    private Integer productId;
    private String name;
    private int priceId;
    private String description;
    private int manufacturerId;
    private int categoryId;
    private BigDecimal priceNow;
    private String priceNameNow;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime priceStartAtNow;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime priceEndAtNow;
    private Integer stock;
    private List<DetailProductResponseDTO> listDetail;
    private List<String> image;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime createAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime updateAt;
    private int weight;
    private int height;
    private int width;
    private int length;
    private List<PriceResponseDTO> listPrice;
    private int isPresent;
}
