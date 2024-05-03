package com.example.orderservice.controller;

import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class TestController {
    @Autowired
    OrderService orderService;
    @RequestMapping(value = "/test-order", method = RequestMethod.GET)
    public ResponseEntity<?> testOrder(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<?> getOrder(){
        Map<String, String> tmp = orderService.placeOrder();
        return new ResponseEntity<>(tmp, HttpStatus.OK);
    }
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResponseEntity<?> putOrder(){
        Map<String, String> tmp = new HashMap<>();
        tmp.put("title", "Phuong thuc post order");
        return new ResponseEntity<>(tmp, HttpStatus.OK);
    }
}
