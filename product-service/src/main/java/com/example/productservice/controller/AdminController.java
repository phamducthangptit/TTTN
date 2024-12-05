package com.example.productservice.controller;

import com.example.productservice.dto.RevenueRequestDTO;
import com.example.productservice.service.OrderService;
import com.example.productservice.service.PredictService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("api/product-service/admin/")
public class AdminController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private PredictService predictService;


    @PostMapping("revenue")
    public ResponseEntity<?> revenue(@RequestHeader("X-Role") String role, @RequestBody RevenueRequestDTO revenueRequestDTO) {
        if (role.equals("ADMIN") || role.equals("EMPLOYEE")) {
            System.out.println(revenueRequestDTO.getStartDate());
            System.out.println(revenueRequestDTO.getEndDate());
            return new ResponseEntity<>(orderService.getRevenue(revenueRequestDTO), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("order-statistics")
    public ResponseEntity<?> orderStatistics(@RequestHeader("X-Role") String role, @RequestBody RevenueRequestDTO revenueRequestDTO){
        if (role.equals("ADMIN") || role.equals("EMPLOYEE")) {
            return new ResponseEntity<>(orderService.getOrderStatistic(revenueRequestDTO), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("predict-trend")
    public ResponseEntity<?> predictTrend(@RequestHeader("X-Role") String role) throws JsonProcessingException {
        if (role.equals("ADMIN") || role.equals("EMPLOYEE")) {

            return new ResponseEntity<>(predictService.predict(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


}
