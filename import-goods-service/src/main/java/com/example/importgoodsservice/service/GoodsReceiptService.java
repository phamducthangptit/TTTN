package com.example.importgoodsservice.service;

import com.example.importgoodsservice.dto.*;
import com.example.importgoodsservice.entity.*;
import com.example.importgoodsservice.repository.GoodsReceiptRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class GoodsReceiptService {
    @Autowired
    private GoodsReceiptRepository goodsReceiptRepository;

    @Autowired
    private StaffService staffService;

    @Autowired
    private GoodsReceiptDetailService goodsReceiptDetailService;

    @Autowired
    private WarehouseReceiptService warehouseReceiptService;

    @Transactional
    public void addNewGoodsReceipt(GoodsReceiptDTO goodsReceiptDTO) {
        int staffId = staffService.getStaffIdByUserName(goodsReceiptDTO.getUserName());
        Staff staff = new Staff();
        staff.setId(staffId);
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(goodsReceiptDTO.getManufacturerId());
        GoodsReceipt goodsReceipt = new GoodsReceipt();
        goodsReceipt.setManufacturer(manufacturer);
        goodsReceipt.setStaff(staff);
        goodsReceipt.setCreateAt(LocalDateTime.now());
        goodsReceipt.setIsComplete(false);
        // save goods receipt
        GoodsReceipt savedReceipt = goodsReceiptRepository.save(goodsReceipt);
        // save goods receipt detail
        int goodsReceiptId = savedReceipt.getId();
        List<GoodsReceiptDetailDTO> listGoodsReceiptDTO = new ArrayList<>();

        // Sử dụng một Map để lưu productId và GoodsReceiptDetailDTO
        Map<Integer, GoodsReceiptDetailDTO> productMap = new HashMap<>();

        for (GoodsReceiptDetailDTO dto : goodsReceiptDTO.getListGoodsReceiptDetail()) {
            // Kiểm tra xem productId đã có trong Map hay chưa
            if (productMap.containsKey(dto.getProductId())) {
                // Nếu đã có, cộng dồn quantity
                GoodsReceiptDetailDTO existingDTO = productMap.get(dto.getProductId());
                existingDTO.setQuantity(existingDTO.getQuantity() + dto.getQuantity());
            } else {
                // Nếu chưa có, thêm vào Map
                productMap.put(dto.getProductId(), dto);
            }
        }
        // Thêm tất cả các giá trị trong Map vào listGoodsReceiptDTO
        listGoodsReceiptDTO.addAll(productMap.values());
        for (GoodsReceiptDetailDTO dto : listGoodsReceiptDTO) {
            goodsReceiptDetailService.saveGoodsReceiptDetail(goodsReceiptId, dto);
        }
    }

    public List<GoodsReceiptResponseDTO> getAllGoodsReceipts(int type) {
        List<GoodsReceipt> goodsReceipts = goodsReceiptRepository.getAllByIsComplete(type == 1);
        List<GoodsReceiptResponseDTO> goodsReceiptResponseDTOs = new ArrayList<>();
        for(GoodsReceipt goodsReceipt : goodsReceipts) {
            GoodsReceiptResponseDTO goodsReceiptResponseDTO = new GoodsReceiptResponseDTO();
            goodsReceiptResponseDTO.setGoodsReceiptId(goodsReceipt.getId());
            goodsReceiptResponseDTO.setFullNameStaff(goodsReceipt.getStaff().getFirstName() + " " + goodsReceipt.getStaff().getLastName());
            goodsReceiptResponseDTO.setManufacturerName(goodsReceipt.getManufacturer().getName());
            // tinh tong so san pham
            goodsReceiptResponseDTO.setTotalProduct(goodsReceipt.getGoodsReceiptDetails().stream().mapToInt(GoodsReceiptDetail::getQuantity).sum());
            // tinh tong tien
            BigDecimal totalPrice = goodsReceipt.getGoodsReceiptDetails().stream()
                    .map(detail -> detail.getPrice().multiply(new BigDecimal(detail.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            goodsReceiptResponseDTO.setTotalPrice(totalPrice);
            goodsReceiptResponseDTO.setCreateAt(goodsReceipt.getCreateAt());
            goodsReceiptResponseDTOs.add(goodsReceiptResponseDTO);
        }
        return goodsReceiptResponseDTOs;
    }

    public int deleteGoodsReceiptById(int goodsReceiptId) {
        // check warehouse receipt, nếu đã có phiếu nhập kho thì không cho sửa xóa
        if(warehouseReceiptService.getByGoodsReceiptId(goodsReceiptId).isEmpty()) {
            // bang null --> chua co phieu nhap kho, tien hanh xoa detail roi xoa phieu
            // xoa goods receipt detail
            goodsReceiptDetailService.deleteByGoodsReceiptId(goodsReceiptId);
            // xoa goodsreceipt
            goodsReceiptRepository.deleteById(goodsReceiptId);
            return 1;
        }
        return -1;
    }

    public void changeStatus(int goodsReceiptId){
        goodsReceiptRepository.updateIsComplete(goodsReceiptId);
    }

    public GoodsReceiptDTO getGoodsReceiptById(int goodsReceiptId) {
        GoodsReceipt goodsReceipt = goodsReceiptRepository.findByid(goodsReceiptId);
        GoodsReceiptDTO goodsReceiptDTO = new GoodsReceiptDTO();
        List<GoodsReceiptDetailDTO> listGoodsReceiptDetailDTO = new ArrayList<>();
        for(GoodsReceiptDetail goodsReceiptDetail : goodsReceipt.getGoodsReceiptDetails()) {
            GoodsReceiptDetailDTO goodsReceiptDetailDTO = new GoodsReceiptDetailDTO();
            goodsReceiptDetailDTO.setProductId(goodsReceiptDetail.getProduct().getId());
            goodsReceiptDetailDTO.setProductName(goodsReceiptDetail.getProduct().getName());
            goodsReceiptDetailDTO.setQuantity(goodsReceiptDetail.getQuantity());
            goodsReceiptDetailDTO.setPrice(goodsReceiptDetail.getPrice());
            listGoodsReceiptDetailDTO.add(goodsReceiptDetailDTO);
        }
        goodsReceiptDTO.setListGoodsReceiptDetail(listGoodsReceiptDetailDTO);
        return goodsReceiptDTO;
    }

    public GoodsReceiptAndWarehouseReceiptDTO getGoodsReceiptAndWarehouseReceiptById(int goodsReceiptId) {
        GoodsReceiptAndWarehouseReceiptDTO goodsReceiptAndWarehouseReceiptDTO = new GoodsReceiptAndWarehouseReceiptDTO();
        List<GoodsReceiptAndWarehouseReceiptDetailDTO> listGoodsReceiptAndWarehouseReceiptDetailDTO = new ArrayList<>();
        GoodsReceipt goodsReceipt = goodsReceiptRepository.findByid(goodsReceiptId); // đơn nhập hàng
        List<WarehouseReceipt> listWarehouseReceipt = warehouseReceiptService.getByGoodsReceiptId(goodsReceiptId); // phiếu nhập kho
        goodsReceiptAndWarehouseReceiptDTO.setFullNameCreateGoodsReceipt(goodsReceipt.getStaff().getFirstName() + " " + goodsReceipt.getStaff().getLastName());
        goodsReceiptAndWarehouseReceiptDTO.setManufacturerName(goodsReceipt.getManufacturer().getName());
        goodsReceiptAndWarehouseReceiptDTO.setDateCreateGoodsReceipt(goodsReceipt.getCreateAt());
        for(GoodsReceiptDetail goodsReceiptDetail : goodsReceipt.getGoodsReceiptDetails()) {
            GoodsReceiptAndWarehouseReceiptDetailDTO tmp = new GoodsReceiptAndWarehouseReceiptDetailDTO();
            tmp.setProductId(goodsReceiptDetail.getProduct().getId());
            tmp.setProductName(goodsReceiptDetail.getProduct().getName());
            tmp.setOrderQuantity(goodsReceiptDetail.getQuantity());
            tmp.setOrderPrice(goodsReceiptDetail.getPrice());
            listGoodsReceiptAndWarehouseReceiptDetailDTO.add(tmp);
        }
        List<WarehouseReceiptDetail> listWarehouseReceiptDetail = new ArrayList<>();
        List<String> listName = new ArrayList<>();
        Set<String> listSetName = new HashSet<>();
        for(WarehouseReceipt warehouseReceipt : listWarehouseReceipt) {
            listWarehouseReceiptDetail.addAll(warehouseReceipt.getWarehouseReceiptDetails());
            listSetName.add(warehouseReceipt.getStaff().getFirstName() + " " + warehouseReceipt.getStaff().getLastName());
        }
        listName.addAll(listSetName);
        for(WarehouseReceiptDetail warehouseReceiptDetail : listWarehouseReceiptDetail) {
            for(GoodsReceiptAndWarehouseReceiptDetailDTO tmp : listGoodsReceiptAndWarehouseReceiptDetailDTO) {
                if(warehouseReceiptDetail.getProduct().getId().equals(tmp.getProductId())) {
                    tmp.setReceivedQuantity(warehouseReceiptDetail.getQuantity());
                    tmp.setDateCreateWarehouseReceipt(warehouseReceiptDetail.getWarehouseReceipt().getCreateAt());
                }
            }
        }
        goodsReceiptAndWarehouseReceiptDTO.setFullNameCreateWarehouseReceipt(listName);
        goodsReceiptAndWarehouseReceiptDTO.setListDetail(listGoodsReceiptAndWarehouseReceiptDetailDTO);
        return goodsReceiptAndWarehouseReceiptDTO;
    }

}
