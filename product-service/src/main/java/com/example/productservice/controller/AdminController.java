package com.example.productservice.controller;

import com.example.productservice.dto.RevenueRequestDTO;
import com.example.productservice.service.OrderService;
import com.example.productservice.service.PredictService;
import com.example.productservice.service.SeriService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/product-service/admin/")
public class AdminController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private PredictService predictService;

    @Autowired
    private SeriService seriService;


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

    @PostMapping("predict-trend-7days")
    public ResponseEntity<?> predictTrend7Days(@RequestHeader("X-Role") String role) throws JsonProcessingException {
        if (role.equals("ADMIN") || role.equals("EMPLOYEE")) {

            return new ResponseEntity<>(predictService.predict7Days(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("predict-trend-30days")
    public ResponseEntity<?> predictTrend30Days(@RequestHeader("X-Role") String role) throws JsonProcessingException {
        if (role.equals("ADMIN") || role.equals("EMPLOYEE")) {

            return new ResponseEntity<>(predictService.predict30Days(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("create-seri")
    public ResponseEntity<?> createAutoSeri(@RequestHeader("X-Role") String role, @RequestParam("productId") int productId, @RequestParam("quantity") int quantity) {
        if (role.equals("ADMIN") || role.equals("EMPLOYEE")) {
            seriService.createSeriAuto(quantity, productId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
