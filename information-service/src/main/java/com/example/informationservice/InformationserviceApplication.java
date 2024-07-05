package com.example.informationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class InformationserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InformationserviceApplication.class, args);
    }
}
