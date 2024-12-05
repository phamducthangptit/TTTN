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
public class GoodsReceiptAndWarehouseReceiptDetailDTO {
    private static final DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###");
    private int productId;
    private String productName;
    private int orderQuantity;
    private BigDecimal orderPrice;
    private int receivedQuantity;
    private BigDecimal receivedPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime dateCreateWarehouseReceipt;
}
