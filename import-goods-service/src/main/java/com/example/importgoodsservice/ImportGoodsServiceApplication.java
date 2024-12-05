package com.example.importgoodsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ImportGoodsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImportGoodsServiceApplication.class, args);
    }
}
