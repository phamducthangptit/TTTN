package com.example.productservice.service;

import com.example.productservice.entity.Order;
import com.example.productservice.entity.OrderDetail;
import com.example.productservice.entity.Product;
import com.example.productservice.entity.Seri;
import com.example.productservice.repository.SeriRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SeriService {
    @Autowired
    private SeriRepository seriRepository;

    @Transactional
    public void createSeriAuto(int quantity, int productId) {
        List<Seri> seris = new ArrayList<>();
        Product product = new Product();
        product.setProductId(productId);
        for (int i = 0; i < quantity; i++) {
            Seri seri = new Seri();
            seri.setProduct(product);
            seri.setSeriNumber(UUID.randomUUID().toString());
            seri.setCreateAt(LocalDateTime.now());
            seris.add(seri);
        }
        seriRepository.saveAll(seris);
    }

    public List<Seri> getSeri(int productId, int n) {
        // Lấy danh sách seri liên quan đến sản phẩm
        List<Seri> seriList = seriRepository.getListSeriByProductId(productId);
        List<Seri> result = new ArrayList<>();

        // Duyệt qua từng số seri để kiểm tra
        for (Seri seri : seriList) {
            boolean isUsedInCompletedOrder = false;

            // Lấy danh sách chi tiết đơn hàng của số seri
            List<OrderDetail> orderDetails = seri.getOrderDetails();

            // Kiểm tra trạng thái của đơn hàng liên quan đến OrderDetail
            for (OrderDetail orderDetail : orderDetails) {
                Order order = orderDetail.getOrder();
                if (!"Hủy".equalsIgnoreCase(order.getStatus())) {
                    isUsedInCompletedOrder = true;
                    break;
                }
            }

            // Nếu số seri này chưa được sử dụng trong bất kỳ đơn hàng "Hoàn thành" nào, thêm vào kết quả
            if (!isUsedInCompletedOrder) {
                result.add(seri);
                if (result.size() == n) {
                    break; // Dừng khi đủ số lượng yêu cầu
                }
            }
        }

        return result; // Trả về danh sách các seri thỏa mãn
    }

    public void deleteSeriByProductId(int productId) {
        seriRepository.deleteSeriByProductId(productId);
    }

}
