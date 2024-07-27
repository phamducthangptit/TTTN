package com.example.productservice.controller;

import com.example.productservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("api/product-service/admin/")
public class AdminController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/revenue-this-week")
    public ResponseEntity<?> getRevenueLast7Days(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                       @RequestHeader("X-Role") String role) {
        if(role.equals("ADMIN"))
            return new ResponseEntity<>(orderService.getRevenueForThisWeek(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/revenue-last-5-weeks")
    public ResponseEntity<?> getRevenueLast5Weeks(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                        @RequestHeader("X-Role") String role) {
        if(role.equals("ADMIN"))
            return new ResponseEntity<>(orderService.getRevenueForLast5Weeks(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/revenue-last-3-months")
    public ResponseEntity<?> getRevenueLast3Months(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                         @RequestHeader("X-Role") String role) {
        if(role.equals("ADMIN"))
            return new ResponseEntity<>(orderService.getRevenueForLast3Months(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/status-counts/this-week")
    public ResponseEntity<?> getOrderCountByStatusForThisWeek(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                              @RequestHeader("X-Role") String role) {
        if(role.equals("ADMIN"))
            return new ResponseEntity<>(orderService.getOrderCountByStatusForThisWeek(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/status-counts/last-five-weeks")
    public ResponseEntity<?> getOrderCountByStatusForLast5Weeks(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                @RequestHeader("X-Role") String role) {
        if(role.equals("ADMIN"))
            return new ResponseEntity<>(orderService.getOrderCountByStatusForLast5Weeks(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/status-counts/last-three-months")
    public ResponseEntity<?> getOrderCountByStatusForLast3Months(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                                 @RequestHeader("X-Role") String role) {
        if(role.equals("ADMIN"))
            return new ResponseEntity<>(orderService.getOrderCountByStatusForLast3Months(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
