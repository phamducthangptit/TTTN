package com.example.importgoodsservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsReceiptDTO {
    private String userName;
    private int manufacturerId;
    private List<GoodsReceiptDetailDTO> listGoodsReceiptDetail;
}
