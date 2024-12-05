package com.example.importgoodsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseReceiptDTO {
    private String userName;
    private List<WarehouseReceiptDetailDTO> listWarehouseReceiptDetail;
}
