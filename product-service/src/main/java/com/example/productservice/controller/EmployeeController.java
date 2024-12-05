package com.example.productservice.controller;

import com.example.productservice.dto.*;
import com.example.productservice.entity.Category;
import com.example.productservice.entity.CategoryDetail;
import com.example.productservice.entity.Detail;
import com.example.productservice.entity.Manufacturer;
import com.example.productservice.id.CategoryDetailId;
import com.example.productservice.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/product-service/employee")
public class EmployeeController {
    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DetailService detailService;

    @Autowired
    private CategoryDetailService categoryDetailService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private PriceService priceService;

    @GetMapping("/manufacturer/get-all-manufacturer")
    public ResponseEntity<?> getAllManufacturer(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                @RequestHeader("X-Role") String role) {
        if (role.equals("EMPLOYEE"))
            return new ResponseEntity<>(manufacturerService.getAllManufacturer(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/manufacturer/add-manufacturer")
    public ResponseEntity<?> addManufacturer(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                             @RequestHeader("X-Role") String role, @RequestBody ManufacturerDTO manufacturerDTO) {
        if (role.equals("EMPLOYEE")) {
            Manufacturer manufacturer = manufacturerService.addNewManufacturer(manufacturerDTO);
            return new ResponseEntity<>(manufacturer, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/manufacturer/update-manufacturer")
    public ResponseEntity<?> updateManufacturer(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                @RequestHeader("X-Role") String role, @RequestBody ManufacturerUpdateDTO manufacturerUpdateDTO) {
        if (role.equals("EMPLOYEE")) {
            manufacturerService.updateManufacturer(manufacturerUpdateDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/manufacturer/delete-manufacturer")
    public ResponseEntity<?> deleteManufacturer(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                @RequestHeader("X-Role") String role, @RequestParam("id") int id) {
        if(role.equals("EMPLOYEE")){
            int result = manufacturerService.deleteManufacturer(id);
            if (result == 1) {
                return new ResponseEntity<>(new ResponseDTO("deleteManufacturerOk", "Xóa nhà sản xuất thành công"), HttpStatus.OK);
            }
            if (result == -2)
                return new ResponseEntity<>(new ResponseDTO("deleteManufacturerError", "Mã nhà sản xuất không tông tại!"), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(new ResponseDTO("deleteManufacturerError", "Xóa nhà sản xuất thất bại vì đã cung cấp sản phẩm!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/category/get-all-category")
    public ResponseEntity<?> getAllCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                            @RequestHeader("X-Role") String role) {
        if (role.equals("EMPLOYEE")) {
            return new ResponseEntity<>(categoryService.getAllCategory(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @Transactional
    @PostMapping("/category/add-category")
    public ResponseEntity<?> addCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                         @RequestHeader("X-Role") String role, @RequestBody CategoryDTO categoryDTO) {
        if (role.equals("EMPLOYEE")) {
            Category category = categoryService.addNewCategory(categoryDTO); // insert khóa chính trước
            if (category != null) {
                System.out.println(category.getCategoryId());
                // add category detail
                List<DetailResponseDTO> listCategoryDetail = new ArrayList<>();
                for (CategoryDetailDTO categoryDetailDTO : categoryDTO.getCategoryDetails()) {
                    CategoryDetail categoryDetail = categoryDetailService.addNewCategoryDetail(category.getCategoryId(), categoryDetailDTO.getDetailId());
                    System.out.println("category id: " + category.getCategoryId());
                    System.out.println("detail id: " + categoryDetailDTO.getDetailId());
                    DetailResponseDTO detailResponseDTO = new DetailResponseDTO();
                    detailResponseDTO.setDetailId(categoryDetailDTO.getDetailId());
                    listCategoryDetail.add(detailResponseDTO);
                }
                CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
                categoryResponseDTO.setCategoryId(category.getCategoryId());
                categoryResponseDTO.setName(category.getName());
                categoryResponseDTO.setImage(category.getImage());
                categoryResponseDTO.setDetailList(listCategoryDetail);
                return new ResponseEntity<>(categoryResponseDTO, HttpStatus.OK); // add category
            }
            return new ResponseEntity<>(new ResponseDTO("ErrorNameCategory", "Tên loại sản phẩm đã tồn tại!"), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/category/update-category")
    public ResponseEntity<?> updateCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                            @RequestHeader("X-Role") String role, @RequestBody CategoryUpdateDTO categoryUpdateDTO) {
        if (role.equals("EMPLOYEE")) {
            int categoryId = categoryUpdateDTO.getCategoryId();
            return new ResponseEntity<>(categoryService.updateCategory(categoryUpdateDTO), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/category/delete-category")
    public ResponseEntity<?> deleteCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                            @RequestHeader("X-Role") String role, @RequestParam("id") int id) {
        int result = categoryService.deleteCategory(id);
        if (result == -1)
            return new ResponseEntity<>(new ResponseDTO("DeleteCategoryError", "Mã loại sản phẩm không tồn tại!"), HttpStatus.BAD_REQUEST);
        if (result == -2)
            return new ResponseEntity<>(new ResponseDTO("DeleteCategoryError", "Xóa loại sản phẩm thất bại vì đã tồn tại sản phẩm!"), HttpStatus.CONFLICT);
        return new ResponseEntity<>(new ResponseDTO("DeleteCategoryOk", "Xóa loại sản phẩm thành công"), HttpStatus.OK);
    }

    @GetMapping("/detail/get-all-detail")
    public ResponseEntity<?> getAllDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                          @RequestHeader("X-Role") String role) {
        if (role.equals("EMPLOYEE"))
            return new ResponseEntity<>(detailService.getAllDetail(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/detail/add-detail")
    public ResponseEntity<?> addDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                       @RequestHeader("X-Role") String role, @RequestBody DetailDTO detailDTO) {
        if (role.equals("EMPLOYEE")) {
            Detail detail = detailService.addNewDetail(detailDTO);
            if (detail != null)
                return new ResponseEntity<>(detail, HttpStatus.OK);
            return new ResponseEntity<>(new ResponseDTO("ErrorDetail", "T   ên thuộc tính đã tồn tại!"), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/product/get-all-product-by-category")
    public ResponseEntity<?> getAllProduct(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                           @RequestHeader("X-Role") String role, @RequestParam("id") int id) {
        if (role.equals("EMPLOYEE")) {
            return new ResponseEntity<>(productService.getAllProductByCategoryId(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/category/get-category-detail")
    public ResponseEntity<?> getDetailProduct(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                              @RequestHeader("X-Role") String role, @RequestParam("category-id") int categoryId) {
        if (role.equals("EMPLOYEE")) {
            if (categoryService.getCategoryById(categoryId) != null)
                return new ResponseEntity<>(categoryDetailService.getCategoryDetailByCategoryId(categoryId), HttpStatus.OK);
            return new ResponseEntity<>(new ResponseDTO("ErrorCategoryId", "Id loại sản phẩm không tồn tại"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/product/add-new-product")
    public ResponseEntity<?> addNewProduct(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                           @RequestHeader("X-Role") String role, @RequestBody ProductRequestDTO productRequestDTO){
        if(role.equals("EMPLOYEE")){
            System.out.println(productRequestDTO.toString());
            return new ResponseEntity<>(productService.saveProduct(productRequestDTO), HttpStatus.OK);

//            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/product/update-is-present")
    public ResponseEntity<?> updateIsPresent(@RequestHeader("X-Role") String role, @RequestBody ProductUpdateIsPresentRequest productUpdateIsPresentRequest){
        if(role.equals("EMPLOYEE")){
            productService.updateIsPresent(productUpdateIsPresentRequest.getProductId(), productUpdateIsPresentRequest.getIsPresent());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/product/delete-product")
    public ResponseEntity<?> deleteProduct(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                           @RequestHeader("X-Role") String role, @RequestParam("id") int productId){
        if(role.equals("EMPLOYEE")){
            int result = productService.deleteProduct(productId);
            if(result == -1)
                return new ResponseEntity<>(new ResponseDTO("ErrorDeleteProduct", "Sản phẩm không tồn tại"), HttpStatus.BAD_REQUEST);
            if(result == -2)
                return new ResponseEntity<>(new ResponseDTO("ErrorDeleteProduct", "Không thể xóa sản phẩm"), HttpStatus.CONFLICT);
            if(result == 1)
                return new ResponseEntity<>(HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/product/product-detail")
    public ResponseEntity<?> detailProductById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                               @RequestHeader("X-Role") String role, @RequestParam("id") int productId){
        if (role.equals("EMPLOYEE")){
            return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/product/update-product")
    public ResponseEntity<?> updateProduct(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                           @RequestHeader("X-Role") String role, @RequestBody ProductRequestUpdateDTO productRequestUpdateDTO){
        if(role.equals("EMPLOYEE")){
            productService.updateProduct(productRequestUpdateDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/order/get-all-order-by-type")
    public ResponseEntity<?> getAllOrderByType(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                               @RequestHeader("X-Role") String role, @RequestParam("type") String type){
        if(role.equals("EMPLOYEE"))
            return new ResponseEntity<>(orderService.getAllOrderByType(type), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/order/update-status-order")
    public ResponseEntity<?> updateStatusOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                               @RequestHeader("X-Role") String role, @RequestBody OrderIdDTO orderIdDTO){
        if(role.equals("EMPLOYEE")){
            orderService.employeeUpdateStatusOrder(orderIdDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/price/get-all-price-name")
    public ResponseEntity<?> getAllPriceName(@RequestHeader("X-Role") String role){
        if(role.equals("EMPLOYEE")){
            List<PriceDTO> listPrice = priceService.getAllPrice();
            if(listPrice != null){
                return new ResponseEntity<>(listPrice, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/price/add-new-price")
    public ResponseEntity<?> addNewPrice(@RequestHeader("X-Role") String role, @RequestBody PriceRequestDTO priceRequestDTO){
        if(role.equals("EMPLOYEE")){
            PriceDTO priceDTO = priceService.savePrice(priceRequestDTO.getPriceName());
            if(priceDTO != null){
                return new ResponseEntity<>(priceDTO, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/price/update-price-product")
    public ResponseEntity<?> updatePriceProduct(@RequestHeader("X-Role") String role, @RequestBody PriceUpdateRequest priceUpdateRequest){
        if(role.equals("EMPLOYEE")){
            System.out.println(priceUpdateRequest.toString());
            priceService.updatePriceProduct(priceUpdateRequest);
            return new ResponseEntity<>((new ResponseDTO("Ok", "Test ok")), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
