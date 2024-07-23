package com.example.productservice.service;

import com.example.productservice.dto.CartRequestOrderDTO;
import com.example.productservice.dto.OrderDetailResponseDTO;
import com.example.productservice.dto.OrderRequestDTO;
import com.example.productservice.dto.OrderResponseDTO;
import com.example.productservice.entity.Image;
import com.example.productservice.entity.Order;
import com.example.productservice.entity.OrderDetail;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.OrderDetailRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository repository;

    public int getQuantityProductInOrder(int productId){
        return repository.getQuantityProductInOrder(productId);
    }

    @Transactional
    public void addNewOrderDetail(OrderRequestDTO orderRequestDTO, Order order){

        for(CartRequestOrderDTO cartRequestOrderDTO : orderRequestDTO.getListProducts()){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            Product product = new Product();
            product.setProductId(cartRequestOrderDTO.getProductId());
            orderDetail.setProduct(product);
            orderDetail.setQuantity(cartRequestOrderDTO.getQuantity());
            orderDetail.setPrice(BigDecimal.valueOf(cartRequestOrderDTO.getPrice()));
            repository.save(orderDetail);
        }
    }

    public List<OrderDetailResponseDTO> getOrderDetail(int orderId){
        return repository.findAllByOrder_orderId(orderId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private OrderDetailResponseDTO convertToDTO(OrderDetail orderDetail) {
        OrderDetailResponseDTO orderDetailResponseDTO = new OrderDetailResponseDTO();
        orderDetailResponseDTO.setProductId(orderDetail.getProduct().getProductId());
        String image = orderDetail.getProduct().getImages().stream()
                .filter(img -> img.isAvatar()) // Lọc các ảnh có avatar là true
                .findFirst() // Lấy ảnh đầu tiên thỏa mãn điều kiện
                .map(Image::getUrl) // Lấy URL của ảnh từ Optional<Image>
                .orElse(null);
        orderDetailResponseDTO.setImage(image);
        orderDetailResponseDTO.setStock(orderDetail.getQuantity());
        orderDetailResponseDTO.setPrice(orderDetail.getPrice());
        if(orderDetail.getOrder().getStatus().equals("Đã nhận hàng")) orderDetailResponseDTO.setCheckStatus(1); else  orderDetailResponseDTO.setCheckStatus(0);
        return orderDetailResponseDTO;
    }
}