package com.example.productservice.controller;

import com.example.productservice.dto.*;
import com.example.productservice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-service/guest")
public class GuestController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private AddressService addressService;

    @GetMapping("/category/get-all-category")
    public ResponseEntity<?> getAllCategory() {
        List<CategoryResponseDTO> listCategoryDTO = categoryService.getAllCategory();
        for (CategoryResponseDTO categoryResponseDTO : listCategoryDTO) {
            categoryResponseDTO.setDetailList(null);
        }
        return new ResponseEntity<>(listCategoryDTO, HttpStatus.OK);
    }

    @GetMapping("/product/get-all-product")
    public ResponseEntity<?> getAllProduct() {
        return new ResponseEntity<>(productService.getAllProduct(), HttpStatus.OK);
    }

    @GetMapping("/product/get-all-product-by-query")
    public ResponseEntity<?> getAllProductByQuery(@RequestParam("query") String query) {
        return new ResponseEntity<>(productService.getAllProductByQuery(query), HttpStatus.OK);
    }

    @GetMapping("/product/get-product-by-category")
    public ResponseEntity<?> getProductByDetail(@RequestParam("category-id") int categoryId) {
        return new ResponseEntity<>(productService.getAllProductByCategoryId(categoryId), HttpStatus.OK);
    }

    @GetMapping("/product/get-product-detail")
    public ResponseEntity<?> getProductDetailById(@RequestParam("id") int id) {
        return new ResponseEntity<>(productService.getProductGuest(id), HttpStatus.OK);
    }

    @GetMapping("/cart/count-product")
    public ResponseEntity<?> countProductInCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                @RequestHeader("X-Role") String role, @RequestParam("username") String userName) {
        if (role.equals("GUEST")) {
            return new ResponseEntity<>(new ResponseDTO("CartCount", cartService.countProductInCart(userName) + ""), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/cart/add-product")
    public ResponseEntity<?> addProductToCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                              @RequestHeader("X-Role") String role, @RequestBody CartRequestDTO cartRequestDTO) {
        if (role.equals("GUEST")) {
            int result = cartService.addProductToCart(cartRequestDTO);
            if (result == 1) {
                return new ResponseEntity<>(new ResponseDTO("AddToCartOk", "Thêm sản phẩm vào giỏ hàng thành công!"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ResponseDTO("AddtoCartError", "Không còn đủ số lượng bạn mong muốn!"), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(new ResponseDTO("ErrorUser", "Bạn cần đăng nhập với tư cách là khách để mua sản phẩm!"), HttpStatus.FORBIDDEN);
    }

    @GetMapping("/cart/get-all-product-in-cart")
    public ResponseEntity<?> getAllProductInCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                 @RequestHeader("X-Role") String role, @RequestParam("username") String userName) {
        if (role.equals("GUEST"))
            return new ResponseEntity<>(cartService.getAllProductInCart(userName), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/cart/delete-product")
    public ResponseEntity<?> deleteProductInCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                 @RequestHeader("X-Role") String role,
                                                 @RequestParam("username") String userName,
                                                 @RequestParam("productId") int productId) {
        if (role.equals("GUEST")) {
            cartService.deleteProductInCart(userName, productId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/cart/update-quantity")
    public ResponseEntity<?> updateQuantityProductInCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                         @RequestHeader("X-Role") String role, @RequestBody CartRequestUpdateDTO cartRequestUpdateDTO) {
        if (role.equals("GUEST")) {
            cartService.updateQuantityProductInCart(cartRequestUpdateDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/order/add-new-order")
    public ResponseEntity<?> addNewOrder(@RequestHeader("X-Role") String role, @RequestBody OrderRequestDTO orderRequestDTO) {
        if (role.equals("GUEST")) {
            int result = orderService.addOrderGuest(orderRequestDTO);
//            System.out.println(orderRequestDTO.toString());
//            int result = 0;
            if (result != 0 && result != -1) // order thành công
                return new ResponseEntity<>(new ResponseDTO("OrderOk", result + ""), HttpStatus.OK);
            if (result == -1)
                return new ResponseEntity<>(new ResponseDTO("OrderError", "Vui lòng kiểm tra lại sản phẩm, có sản phẩm đã hết hàng!"), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/order/get-all-order")
    public ResponseEntity<?> getAllOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                         @RequestHeader("X-Role") String role, @RequestParam("username") String userName) {
        if (role.equals("GUEST")) {
            return new ResponseEntity<>(orderService.getAllOrderByUserName(userName), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/order/get-detail-order")
    public ResponseEntity<?> getDetailOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                            @RequestHeader("X-Role") String role, @RequestParam("order-id") int orderId) {
        if (role.equals("GUEST"))
            return new ResponseEntity<>(orderDetailService.getOrderDetail(orderId), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/order/update-status-order")
    public ResponseEntity<?> guestUpdateStatusOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                    @RequestHeader("X-Role") String role, @RequestBody OrderIdDTO orderIdDTO) {
        if (role.equals("GUEST")) {
            orderService.guestUpdateStatusOrder(orderIdDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/review/add-review")
    public ResponseEntity<?> addReview(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                       @RequestHeader("X-Role") String role, @RequestBody ReviewRequestDTO reviewRequestDTO) {
        if (role.equals("GUEST")) {
            return new ResponseEntity<>(reviewService.addNewReview(reviewRequestDTO), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/payment/update-status-payment")
    public ResponseEntity<?> updateStatusPayment(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                 @RequestHeader("X-Role") String role, @RequestBody PaymentRequest paymentRequest) {
        if (role.equals("GUEST")) {
            orderService.updateStatusPayment(paymentRequest.getOrderId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/address/get-all-address")
    public ResponseEntity<?> getAllAddress(@RequestHeader("X-Role") String role, @RequestParam("username") String userName){
        if(role.equals("GUEST")){
            List<AddressResponseDTO> listResult = addressService.getAllAddressByUserName(userName);
            if(listResult != null)
                return new ResponseEntity<>(listResult, HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/address/delete-address")
    public ResponseEntity<?> deleteAddressById(@RequestHeader("X-Role") String role, @RequestParam("id") int id){
        if(role.equals("GUEST")){
            addressService.deleteAddress(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}
