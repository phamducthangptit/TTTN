package com.example.importgoodsservice.controller;


import com.example.importgoodsservice.dto.*;
import com.example.importgoodsservice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/import-goods-service/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private ProductService productService;
    @Autowired
    private GoodsReceiptService goodsReceiptService;
    @Autowired
    private WarehouseReceiptService warehouseReceiptService;
    @Autowired
    private GoodsReceiptDetailService goodsReceiptDetailService;

    @GetMapping("/get-name")
    public ResponseEntity<?> test(@RequestHeader("X-Role") String role, @RequestParam("user-name") String userName) {
        if(role.equals("EMPLOYEE")){
            String name = employeeService.getNameByUserName(userName);
            Map<String, String> response = new HashMap<>();
            response.put("name", name);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/get-list-manufacturer")
    public ResponseEntity<?> getListManufacturer(@RequestHeader("X-Role") String role){
        if(role.equals("EMPLOYEE")){
            List<ManufacturerDTO> listManufacturerDTO = manufacturerService.getAllManufacturers();
            if(listManufacturerDTO.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(listManufacturerDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/get-list-product")
    public ResponseEntity<?> getListProduct(@RequestHeader("X-Role") String role, @RequestParam("manufacturer-id") int manufacturerId){
        if(role.equals("EMPLOYEE")){
            List<ProductDTO> listProductDTO = productService.getAllProductByManufacturer(manufacturerId);
            if(listProductDTO.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(listProductDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/add-new-import-goods")
    public ResponseEntity<?> addNewImportGoods(@RequestHeader("X-Role") String role, @RequestBody GoodsReceiptDTO goodsReceiptDTO){
        if(role.equals("EMPLOYEE")){
            goodsReceiptService.addNewGoodsReceipt(goodsReceiptDTO);
            System.out.println(goodsReceiptDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/get-all-goods-receipt")
    public ResponseEntity<?> getAllGoodsReceipt(@RequestHeader("X-Role") String role, @RequestParam("type") int type){
        if(role.equals("EMPLOYEE")){
            List<GoodsReceiptResponseDTO> listGoodsReceiptResponseDTO = goodsReceiptService.getAllGoodsReceipts(type);
            if(listGoodsReceiptResponseDTO.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(listGoodsReceiptResponseDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/delete-goods-receipt")
    public ResponseEntity<?> deleteGoodsReceipt(@RequestHeader("X-Role") String role, @RequestParam("goods-receipt-id") int goodsReceiptId){
        if(role.equals("EMPLOYEE")){
            int x = goodsReceiptService.deleteGoodsReceiptById(goodsReceiptId);
            if(x == 1){ // xoa thanh cong
                return new ResponseEntity<>(new ResponseDTO("OkDelete", "Xóa thành công!"), HttpStatus.OK);
            } else if(x == -1){ // xoa that bai
                return new ResponseEntity<>(new ResponseDTO("ErrorDelete", "Xóa thất bại do đã tạo phiếu nhập kho!"), HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/detail-goods-receipt")
    public ResponseEntity<?> detailGoodsReceipt(@RequestHeader("X-Role") String role, @RequestParam("goods-receipt-id") int goodsReceiptId){
        if(role.equals("EMPLOYEE")){
            return new ResponseEntity<>(goodsReceiptService.getGoodsReceiptById(goodsReceiptId), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/change-status-goods-receipt")
    public ResponseEntity<?> changeStatusGoodsReceipt(@RequestHeader("X-Role") String role, @RequestParam("goods-receipt-id") int goodsReceiptId){
        if(role.equals("EMPLOYEE")){
            int x = goodsReceiptService.changeStatus(goodsReceiptId);
            if(x == 1){
                return new ResponseEntity<>(HttpStatus.OK);
            } else if(x == -1){
                return new ResponseEntity<>(new ResponseDTO("ErrorChangeStatus", "Bạn chưa nhập kho nên chưa thể hoàn thành đơn nhập hàng!"), HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    @GetMapping("/get-product-no-warehouse-receipt")
    public ResponseEntity<?> getProductNoWarehouseReceipt(@RequestHeader("X-Role") String role, @RequestParam("goods-receipt-id") int goodsReceiptId){
        if(role.equals("EMPLOYEE")){
            List<ProductDTO> listProductNoWarehouseReceipt = goodsReceiptDetailService.getAllProductNoWarehouseReceipt(goodsReceiptId);
            if(listProductNoWarehouseReceipt.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(listProductNoWarehouseReceipt, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/create-warehouse-receipt")
    public ResponseEntity<?> createWarehouseReceipt(@RequestHeader("X-Role") String role, @RequestBody WarehouseReceiptDTO warehouseReceiptDTO){
        if(role.equals("EMPLOYEE")){
            warehouseReceiptService.addNewWarehouseReceipt(warehouseReceiptDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/get-detail-goods-receipt-and-warehouse-receipt")
    public ResponseEntity<?> getDetailGoodsReceiptAndWarehouseReceipt(@RequestHeader("X-Role") String role, @RequestParam("goods-receipt-id") int goodsReceiptId){
        if(role.equals("EMPLOYEE")){
            return new ResponseEntity<>(goodsReceiptService.getGoodsReceiptAndWarehouseReceiptById(goodsReceiptId), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
