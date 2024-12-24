package com.example.productservice.service;

import com.example.productservice.dto.*;
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

    private List<Integer> listProductId = new ArrayList<>();

    public List<Predict7DaysResponseDTO> predict7Days() throws JsonProcessingException {
        List<Order> listOrders = orderService.getAllOrderPrev7Days();
        String apiUrl = "http://127.0.0.1:9999/get-product-id";
        listProductId.clear();

        RestTemplate restTemplate = new RestTemplate();
        try {
            // Gửi yêu cầu GET và nhận phản hồi
            Map<String, List<Integer>> response = restTemplate.getForObject(apiUrl, Map.class);

            if (response != null && response.containsKey("productId")) {
                listProductId = response.get("productId");
            } else {
                System.out.println("Không nhận được dữ liệu từ API hoặc không có khóa 'productId'.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int[][] ordersMatrix = new int[7][listProductId.size()];
        // group by orderdate
        Map<LocalDate, List<Order>> ordersByDate = listOrders.stream()
                .collect(Collectors.groupingBy(order -> order.getOrderDate().toLocalDate()));
        AtomicInteger i = new AtomicInteger(0);
        // Duyệt qua các nhóm và xử lý
        ordersByDate.forEach((date, ordersOnDate) -> {
            System.out.println("Ngày: " + date);
            ordersOnDate.forEach(order -> {
                for (OrderDetail orderDetail : order.getOrderDetails()) {
                    int productId = orderDetail.getSeri().getProduct().getProductId();
                    if (productId == listProductId.get(0)) {
                        ordersMatrix[i.get()][0] += 1;
                    }
                    if (productId == listProductId.get(1)) {
                        ordersMatrix[i.get()][1] += 1;
                    }
                    if (productId == listProductId.get(2)) {
                        ordersMatrix[i.get()][2] += 1;
                    }
                    if (productId == listProductId.get(3)) {
                        ordersMatrix[i.get()][3] += 1;
                    }
                    if (productId == listProductId.get(4)) {
                        ordersMatrix[i.get()][4] += 1;
                    }
                    if (productId == listProductId.get(5)) {
                        ordersMatrix[i.get()][5] += 1;
                    }
                    if (productId == listProductId.get(6)) {
                        ordersMatrix[i.get()][6] += 1;
                    }
                    if (productId == listProductId.get(7)) {
                        ordersMatrix[i.get()][7] += 1;
                    }
                    if (productId == listProductId.get(8)) {
                        ordersMatrix[i.get()][8] += 1;
                    }
                }
            });
            i.incrementAndGet();
        });

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(Map.of("data", ordersMatrix));
        // Gửi request tới API Python
        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
        apiUrl = "http://localhost:9999/predict-7days";

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);
        PredictionResponse predictionResponse = objectMapper.readValue(response.getBody(), PredictionResponse.class);
        List<List<Double>> predictions = predictionResponse.getTopColumns(); // mang predict
        List<Integer> topColumnsIndices = predictionResponse.getTopColumnsIndices(); // danh sach chi so cot
        int numColumns = topColumnsIndices.size();
        int numRows = 7;

        // Duyệt qua các cột
        List<Predict7DaysResponseDTO> predictResponseList = new ArrayList<>();
        for (int row = 0; row < numRows; row++) {
            Predict7DaysResponseDTO tmp = new Predict7DaysResponseDTO();
            tmp.setDay(row + 1);
            System.out.print("Cột " + (row + 1) + ": ");
            List<ProductPredictResponse> listProduct = new ArrayList<>();
            for (int col = 0; col < numColumns; col++) {
                ProductPredictResponse tmpProduct = new ProductPredictResponse();
                // trị tại dòng `row` và cột `col`
                tmpProduct.setProductName(productService.getProductNameByProductId(listProductId.get(topColumnsIndices.get(col))));
                tmpProduct.setQuantity(predictions.get(row).get(col));
                listProduct.add(tmpProduct);
            }
            tmp.setPredict(listProduct);
            predictResponseList.add(tmp);
        }
        System.out.println(jsonBody);
        return predictResponseList;
    }

    public Predict30DaysResponseDTO predict30Days() throws JsonProcessingException {
        List<Order> listOrders = orderService.getAllOrderPrev30Days();
        String apiUrl = "http://127.0.0.1:9999/get-product-id";
        listProductId.clear();

        RestTemplate restTemplate = new RestTemplate();
        try {
            // Gửi yêu cầu GET và nhận phản hồi
            Map<String, List<Integer>> response = restTemplate.getForObject(apiUrl, Map.class);

            if (response != null && response.containsKey("productId")) {
                listProductId = response.get("productId");
            } else {
                System.out.println("Không nhận được dữ liệu từ API hoặc không có khóa 'productId'.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int[][] ordersMatrix = new int[30][listProductId.size()];
        // group by orderdate
        Map<LocalDate, List<Order>> ordersByDate = listOrders.stream()
                .collect(Collectors.groupingBy(order -> order.getOrderDate().toLocalDate()));
        AtomicInteger i = new AtomicInteger(0);
        // Duyệt qua các nhóm và xử lý
        ordersByDate.forEach((date, ordersOnDate) -> {
            System.out.println("Ngày: " + date);
            ordersOnDate.forEach(order -> {
                for (OrderDetail orderDetail : order.getOrderDetails()) {
                    int productId = orderDetail.getSeri().getProduct().getProductId();
                    if (productId == listProductId.get(0)) {
                        ordersMatrix[i.get()][0] += 1;
                    }
                    if (productId == listProductId.get(1)) {
                        ordersMatrix[i.get()][1] += 1;
                    }
                    if (productId == listProductId.get(2)) {
                        ordersMatrix[i.get()][2] += 1;
                    }
                    if (productId == listProductId.get(3)) {
                        ordersMatrix[i.get()][3] += 1;
                    }
                    if (productId == listProductId.get(4)) {
                        ordersMatrix[i.get()][4] += 1;
                    }
                    if (productId == listProductId.get(5)) {
                        ordersMatrix[i.get()][5] += 1;
                    }
                    if (productId == listProductId.get(6)) {
                        ordersMatrix[i.get()][6] += 1;
                    }
                    if (productId == listProductId.get(7)) {
                        ordersMatrix[i.get()][7] += 1;
                    }
                    if (productId == listProductId.get(8)) {
                        ordersMatrix[i.get()][8] += 1;
                    }
                }
            });
            i.incrementAndGet();
        });

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(Map.of("data", ordersMatrix));
        // Gửi request tới API Python
        restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
        apiUrl = "http://localhost:9999/predict-30days";

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);
        Prediction30DayResponse predict30DaysResponseDTO = objectMapper.readValue(response.getBody(), Prediction30DayResponse.class);
        List<Double> predictions = predict30DaysResponseDTO.getTop_columns_sum();
        List<Integer> topColumnsIndices = predict30DaysResponseDTO.getTop_columns_indices();

        Predict30DaysResponseDTO predictResponse = new Predict30DaysResponseDTO();
        List<ProductPredictResponse> listProductPredictResponse = new ArrayList<>();
        for(int j = 0; j < topColumnsIndices.size(); j++) {
            ProductPredictResponse tmp = new ProductPredictResponse();
            tmp.setProductName(productService.getProductNameByProductId(listProductId.get(topColumnsIndices.get(j))));
            tmp.setQuantity(predictions.get(j));
            listProductPredictResponse.add(tmp);
        }
        predictResponse.setPredict(listProductPredictResponse);
        return predictResponse;
    }
}
