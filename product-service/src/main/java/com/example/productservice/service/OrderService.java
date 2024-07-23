package com.example.productservice.service;

import com.example.productservice.dto.*;
import com.example.productservice.entity.Image;
import com.example.productservice.entity.Order;
import com.example.productservice.entity.OrderDetail;
import com.example.productservice.entity.User;
import com.example.productservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository repository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private CartService cartService;



    public Order addNewOrder(OrderRequestDTO orderRequestDTO){
        Order order = new Order();
        int userId = accountService.getUserId(orderRequestDTO.getUserName());
        User user = new User();
        user.setUserId(userId);
        order.setUser(user);
        order.setConsigneeName(orderRequestDTO.getName());
        order.setTotalAmount(BigDecimal.valueOf(orderRequestDTO.getTotalAmount()));
        order.setAddress(orderRequestDTO.getAddress());
        order.setStatus("Mới");
        order.setPhone(orderRequestDTO.getPhone());
        order.setOrderDate(LocalDateTime.now());
        return repository.save(order);
    }
    @Transactional
    public void addOrderGuest(OrderRequestDTO orderRequestDTO){
        // add khóa chính trước --> add order
        Order order = addNewOrder(orderRequestDTO);
        if(order != null){
            // add khóa phụ sau
            orderDetailService.addNewOrderDetail(orderRequestDTO, order);
        }
        // delete product in cart
        for (CartRequestOrderDTO cartRequestOrderDTO : orderRequestDTO.getListProducts()){
            cartService.deleteProductInCart(orderRequestDTO.getUserName(), cartRequestOrderDTO.getProductId());
        }
    }

    public List<OrderResponseDTO> getAllOrderByUserName(String userName){
        int userId = accountService.getUserId(userName);
        return repository.findAllByUser_userId(userId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private OrderResponseDTO convertToDTO(Order order) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setOrderId(order.getOrderId());
        orderResponseDTO.setTotalAmountPaid(order.getTotalAmount());
        orderResponseDTO.setOrderDate(order.getOrderDate());
        orderResponseDTO.setStatus(order.getStatus());
        orderResponseDTO.setAddress(order.getAddress());
        orderResponseDTO.setPhone(order.getPhone());
        orderResponseDTO.setTotalProduct(totalProduct(order.getOrderDetails()));
        if(order.getStatus().equals("Đã xác nhận")) orderResponseDTO.setCheckStatus(1); else orderResponseDTO.setCheckStatus(0);
        return orderResponseDTO;
    }

    private int totalProduct(List<OrderDetail> orderDetails){
        int count = 0;
        for(OrderDetail orderDetail : orderDetails){
            count += orderDetail.getQuantity();
        }
        return count;
    }

    public List<OrderResponseEmployeeDTO> getAllOrderByType(String type){
        return repository.getAllOrderByType(type).stream().map(this::convertOrderEmployeeToDTO).collect(Collectors.toList());
    }

    public OrderResponseEmployeeDTO convertOrderEmployeeToDTO(Order order) {
        OrderResponseEmployeeDTO orderResponseEmployeeDTO = new OrderResponseEmployeeDTO();
        orderResponseEmployeeDTO.setOrderId(order.getOrderId());
        orderResponseEmployeeDTO.setTotalAmountPaid(order.getTotalAmount());
        orderResponseEmployeeDTO.setOrderDate(order.getOrderDate());
        orderResponseEmployeeDTO.setConsigneeName(order.getConsigneeName());
        orderResponseEmployeeDTO.setAddress(order.getAddress());
        orderResponseEmployeeDTO.setPhone(order.getPhone());
        orderResponseEmployeeDTO.setCheckStatus(order.getStatus().equals("Mới") ? 1 : 0);
        orderResponseEmployeeDTO.setListProducts(order.getOrderDetails().stream().map(this::convertOrderDetailToProduct).collect(Collectors.toList()));
        return orderResponseEmployeeDTO;
    }

    public ProductResponseOrderDTO convertOrderDetailToProduct(OrderDetail orderDetail) {
        ProductResponseOrderDTO productResponseOrderDTO = new ProductResponseOrderDTO();
        productResponseOrderDTO.setProductId(orderDetail.getProduct().getProductId());
        productResponseOrderDTO.setName(orderDetail.getProduct().getName());
        productResponseOrderDTO.setPrice(orderDetail.getProduct().getPrice());
        String image = orderDetail.getProduct().getImages().stream()
                .filter(img -> img.isAvatar()) // Lọc các ảnh có avatar là true
                .findFirst() // Lấy ảnh đầu tiên thỏa mãn điều kiện
                .map(Image::getUrl) // Lấy URL của ảnh từ Optional<Image>
                .orElse(null);
        productResponseOrderDTO.setImage(image);
        productResponseOrderDTO.setQuantity(orderDetail.getQuantity());
        return productResponseOrderDTO;
    }

    public void employeeUpdateStatusOrder(int orderId){
        repository.updateStatusOrder(orderId, "Đã xác nhận");
    }

    public void guestUpdateStatusOrder(int orderId){
        repository.updateStatusOrder(orderId, "Hoàn thành");
    }
}
