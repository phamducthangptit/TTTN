package com.example.importgoodsservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsReceiptAndWarehouseReceiptDTO {
    private String fullNameCreateGoodsReceipt;
    private String manufacturerName;
    private List<String> fullNameCreateWarehouseReceipt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime dateCreateGoodsReceipt;
    private List<GoodsReceiptAndWarehouseReceiptDetailDTO> listDetail;
}
