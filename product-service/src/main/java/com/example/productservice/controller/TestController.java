package com.example.productservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
public class TestController {
    @RequestMapping(value = "/test-product", method = RequestMethod.GET)
    public ResponseEntity<?> testItem(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<?> getOrder(){
        Map<String, String> tmp = new HashMap<>();
        tmp.put("title", "Phuong thuc get product");
        return new ResponseEntity<>(tmp, HttpStatus.OK);
    }
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResponseEntity<?> putOrder(){
        Map<String, String> tmp = new HashMap<>();
        tmp.put("title", "Phuong thuc post product");
        return new ResponseEntity<>(tmp, HttpStatus.OK);
    }
}
