package com.example.importgoodsservice.service;

import com.example.importgoodsservice.dto.WarehouseReceiptDetailDTO;
import com.example.importgoodsservice.entity.Product;
import com.example.importgoodsservice.entity.WarehouseReceipt;
import com.example.importgoodsservice.entity.WarehouseReceiptDetail;
import com.example.importgoodsservice.repository.WarehouseReceiptDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarehouseReceiptDetailService {
    @Autowired
    private WarehouseReceiptDetailRepository warehouseReceiptDetailRepository;

    public void saveWarehouseReceiptDetail(WarehouseReceiptDetailDTO warehouseReceiptDetailDTO, WarehouseReceipt warehouseReceipt) {
        WarehouseReceiptDetail warehouseReceiptDetail = new WarehouseReceiptDetail();
        warehouseReceiptDetail.setWarehouseReceipt(warehouseReceipt);
        Product product = new Product();
        product.setId(warehouseReceiptDetailDTO.getProductId());
        warehouseReceiptDetail.setProduct(product);
        warehouseReceiptDetail.setQuantity(warehouseReceiptDetailDTO.getReceivedQuantity());
        warehouseReceiptDetail.setPrice(warehouseReceiptDetailDTO.getReceivedPrice());
        warehouseReceiptDetailRepository.save(warehouseReceiptDetail);
    }
}
