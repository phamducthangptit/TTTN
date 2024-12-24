package com.example.productservice.service;

import com.example.productservice.dto.CartRequestOrderDTO;
import com.example.productservice.dto.OrderDetailResponseDTO;
import com.example.productservice.dto.OrderRequestDTO;
import com.example.productservice.dto.OrderResponseDTO;
import com.example.productservice.entity.*;
import com.example.productservice.repository.OrderDetailRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository repository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private SeriService seriService;


    public int getQuantityProductInOrder(int productId) {
        return repository.getQuantityProductInOrder(productId);
    }

    @Transactional
    public void addNewOrderDetail(OrderRequestDTO orderRequestDTO, Order order) {
        for (CartRequestOrderDTO cartRequestOrderDTO : orderRequestDTO.getListProducts()) {
            int quantity = cartRequestOrderDTO.getQuantity();
            List<Seri> seriList = seriService.getSeri(cartRequestOrderDTO.getProductId(), quantity);
            // Tạo OrderDetail
            for (Seri seri : seriList) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setSeri(seri);
                orderDetail.setPrice(BigDecimal.valueOf(cartRequestOrderDTO.getPrice()));
                // Lưu OrderDetail
                repository.save(orderDetail);
            }
        }
    }

    public List<OrderDetailResponseDTO> getOrderDetail(int orderId) {
        List<OrderDetail> listOrderDetail = repository.findAllByOrder_orderId(orderId);
        List<OrderDetailResponseDTO> orderDetailResponseDTOS = new ArrayList<>();
        for (OrderDetail orderDetail : listOrderDetail) {
            Optional<OrderDetailResponseDTO> existingDTO = orderDetailResponseDTOS.stream()
                    .filter(od -> od.getProductId() == orderDetail.getSeri().getProduct().getProductId())
                    .findFirst();

            if (existingDTO.isPresent()) { // nếu đã tồn tại thi update so luong
                OrderDetailResponseDTO orderDetailResponseDTO = existingDTO.get();
                orderDetailResponseDTO.setStock(orderDetailResponseDTO.getStock() + 1);
                if(orderDetail.getOrder().getStatus().equals("Hoàn thành")){
                    List<String> listSeri = orderDetailResponseDTO.getListSeri();
                    listSeri.add(orderDetail.getSeri().getSeriNumber());
                    orderDetailResponseDTO.setListSeri(listSeri);
                }
            } else { // chua ton tai thi khoi tao
                OrderDetailResponseDTO orderDetailResponseDTO = convertToDTO(orderDetail);
                if(orderDetail.getOrder().getStatus().equals("Hoàn thành")){
                    List<String> listSeri = new ArrayList<>();
                    listSeri.add(orderDetail.getSeri().getSeriNumber());
                    orderDetailResponseDTO.setListSeri(listSeri);
                }
                orderDetailResponseDTOS.add(orderDetailResponseDTO);
            }
        }
        return orderDetailResponseDTOS;
    }


    private OrderDetailResponseDTO convertToDTO(OrderDetail orderDetail) {
        OrderDetailResponseDTO orderDetailResponseDTO = new OrderDetailResponseDTO();
        int productId = orderDetail.getSeri().getProduct().getProductId();
        orderDetailResponseDTO.setProductId(productId);
        orderDetailResponseDTO.setOrderDetailId(orderDetail.getOrderDetailId());
        String image = orderDetail.getSeri().getProduct().getImages().stream()
                .filter(img -> img.isAvatar()) // Lọc các ảnh có avatar là true
                .findFirst() // Lấy ảnh đầu tiên thỏa mãn điều kiện
                .map(Image::getUrl) // Lấy URL của ảnh từ Optional<Image>
                .orElse(null);
        orderDetailResponseDTO.setImage(image);
        orderDetailResponseDTO.setStock(1);
        orderDetailResponseDTO.setPrice(orderDetail.getPrice());
        orderDetailResponseDTO.setProductName(orderDetail.getSeri().getProduct().getName());
        System.out.println(productId);
        boolean hasReview = reviewService.hasUserReviewed(orderDetail.getOrderDetailId());
        System.out.println(hasReview);
        if (orderDetail.getOrder().getStatus().equals("Hoàn thành")) orderDetailResponseDTO.setCheckStatus(1);
        else orderDetailResponseDTO.setCheckStatus(0);
        if (orderDetail.getOrder().getStatus().equals("Hoàn thành") && !hasReview)
            orderDetailResponseDTO.setCheckReview(1);
        else orderDetailResponseDTO.setCheckReview(0);
        return orderDetailResponseDTO;
    }
}
