package com.example.importgoodsservice.service;

import com.example.importgoodsservice.dto.WarehouseReceiptDTO;
import com.example.importgoodsservice.dto.WarehouseReceiptDetailDTO;
import com.example.importgoodsservice.entity.GoodsReceipt;
import com.example.importgoodsservice.entity.Staff;
import com.example.importgoodsservice.entity.WarehouseReceipt;
import com.example.importgoodsservice.repository.WarehouseReceiptRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WarehouseReceiptService {
    @Autowired
    private WarehouseReceiptRepository warehouseReceiptRepository;

    @Autowired
    private WarehouseReceiptDetailService warehouseReceiptDetailService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private ProductService productService;


    public List<WarehouseReceipt> getByGoodsReceiptId(int goodsReceiptId) {
        return warehouseReceiptRepository.findByGoodsReceiptId(goodsReceiptId);
    }

    @Transactional
    public void addNewWarehouseReceipt(WarehouseReceiptDTO warehouseReceiptDTO) {
        // save warehouse receipt
        int goodsReceiptId = warehouseReceiptDTO.getListWarehouseReceiptDetail().get(0).getGoodsReceiptId();
        WarehouseReceipt warehouseReceipt = new WarehouseReceipt();
        int staffId = staffService.getStaffIdByUserName(warehouseReceiptDTO.getUserName());
        Staff staff = new Staff();
        staff.setId(staffId);
        warehouseReceipt.setStaff(staff);
        GoodsReceipt goodsReceipt = new GoodsReceipt();
        goodsReceipt.setId(goodsReceiptId);
        warehouseReceipt.setGoodsReceipt(goodsReceipt);
        warehouseReceipt.setCreateAt(LocalDateTime.now());
        WarehouseReceipt warehouseReceiptSave = warehouseReceiptRepository.save(warehouseReceipt);

        // save warehouse receipt detail
        for(WarehouseReceiptDetailDTO warehouseReceiptDetailDTO : warehouseReceiptDTO.getListWarehouseReceiptDetail()) {
            warehouseReceiptDetailService.saveWarehouseReceiptDetail(warehouseReceiptDetailDTO, warehouseReceiptSave);
            // update quantity product
            productService.updateStockProduct(warehouseReceiptDetailDTO.getProductId(), warehouseReceiptDetailDTO.getReceivedQuantity());
        }

    }


}
