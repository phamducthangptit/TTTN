package com.example.orderservice.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@Transactional
public class OrderService {
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public OrderService(WebClient.Builder webClientBuilder){
        this.webClientBuilder = webClientBuilder;
    }
    public Map<String, String> placeOrder(){ // connect giữa 2 service
        Map<String, String> map = webClientBuilder.build().post()
                .uri("http://product-service/api/product/test")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        return map;
    }
}
