package com.example.productservice.service;

import com.example.productservice.dto.CategoryPredictResponse;
import com.example.productservice.dto.PredictResponseDTO;
import com.example.productservice.dto.PredictionResponse;
import com.example.productservice.entity.Order;
import com.example.productservice.entity.OrderDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class PredictService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    public List<PredictResponseDTO> predict() throws JsonProcessingException {
        List<Order> listOrders = orderService.getAllOrderPrev7Days();
        int[][] ordersMatrix = new int[7][7];
        // group by orderdate
        Map<LocalDate, List<Order>> ordersByDate = listOrders.stream()
                .collect(Collectors.groupingBy(order -> order.getOrderDate().toLocalDate()));
        AtomicInteger i = new AtomicInteger(0);
        // Duyệt qua các nhóm và xử lý
        ordersByDate.forEach((date, ordersOnDate) -> {
            System.out.println("Ngày: " + date);
            ordersOnDate.forEach(order -> {
                for (OrderDetail orderDetail : order.getOrderDetails()) {
                    String categoryName = orderDetail.getProduct().getCategory().getName();
                    if (categoryName.equals("Laptop")) { // laptop
                        ordersMatrix[i.get()][0] += orderDetail.getQuantity();
                    }
                    if (categoryName.equals("Điện thoại")) { // dien thoai
                        ordersMatrix[i.get()][1] += orderDetail.getQuantity();
                    }
                    if (categoryName.equals("Máy tính bảng") || categoryName.equals("Ipad")) { // ipad
                        ordersMatrix[i.get()][2] += orderDetail.getQuantity();
                    }
                    if (categoryName.equals("Tai nghe") || categoryName.equals("Headphone")) { // tai nghe
                        ordersMatrix[i.get()][3] += orderDetail.getQuantity();
                    }
                    if (categoryName.equals("Bàn phím") || categoryName.equals("Keyboard")) { // ban phim
                        ordersMatrix[i.get()][4] += orderDetail.getQuantity();
                    }
                    if (categoryName.equals("Chuột") || categoryName.equals("Mouse")) { // Chuot
                        ordersMatrix[i.get()][5] += orderDetail.getQuantity();
                    }
                    if (categoryName.equals("Màn hình") || categoryName.equals("Monitor")) { // man hinh
                        ordersMatrix[i.get()][6] += orderDetail.getQuantity();
                    }
                }
            });
            i.incrementAndGet();
        });

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(Map.of("data", ordersMatrix));
        // Gửi request tới API Python
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
        String apiUrl = "http://localhost:9999/predict";

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);
        PredictionResponse predictionResponse = objectMapper.readValue(response.getBody(), PredictionResponse.class);
        List<List<Double>> predictions = predictionResponse.getPredictions();
        int numColumns = predictions.get(0).size();
        int numRows = predictions.size();

        // Duyệt qua các cột
        List<PredictResponseDTO> predictResponseList = new ArrayList<>();
        for (int col = 0; col < numColumns; col++) {
            PredictResponseDTO tmp = new PredictResponseDTO();
            tmp.setDay(col + 1);
            System.out.print("Cột " + (col + 1) + ": ");
            List<CategoryPredictResponse> listProduct = new ArrayList<>();
            for (int row = 0; row < numRows; row++) {
                CategoryPredictResponse tmpProduct = new CategoryPredictResponse();
                // trị tại dòng `row` và cột `col`
                if(row == 0) tmpProduct.setCategoryName("Laptop");
                if(row == 1) tmpProduct.setCategoryName("Điện thoại");
                if(row == 2) tmpProduct.setCategoryName("Máy tính bảng");
                if(row == 3) tmpProduct.setCategoryName("Tai nghe");
                if(row == 4) tmpProduct.setCategoryName("Bàn phím");
                if(row == 5) tmpProduct.setCategoryName("Chuột");
                if(row == 6) tmpProduct.setCategoryName("Màn hình");
                tmpProduct.setQuantity(predictions.get(row).get(col));
                listProduct.add(tmpProduct);
            }
            tmp.setPredict(listProduct);
            predictResponseList.add(tmp);
        }
        System.out.println(jsonBody);
        return predictResponseList;
    }
}
