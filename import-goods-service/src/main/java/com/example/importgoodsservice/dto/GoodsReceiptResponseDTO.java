package com.example.importgoodsservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoodsReceiptResponseDTO {
    private static final DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###");
    private int goodsReceiptId;
    private String fullNameStaff;
    private String manufacturerName;
    private int totalProduct;
    private BigDecimal totalPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime createAt;
}
