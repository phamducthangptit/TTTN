package com.example.orderservice.controller;

import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class TestController {
    @Autowired
    OrderService orderService;
    @RequestMapping(value = "/test-admin", method = RequestMethod.GET)
    public ResponseEntity<?> testOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                       @RequestHeader("X-Role") String role, @RequestParam("test") String test){
        if(role.equals("ADMIN"))
            return new ResponseEntity<>(test, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/test-nhanvien", method = RequestMethod.GET)
    public ResponseEntity<?> getOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                      @RequestHeader("X-Role") String role, @RequestParam("test") String test){
        if(role.equals("EMPLOYEE") || role.equals("GUEST"))
            return new ResponseEntity<>(test, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    @RequestMapping(value = "/test-khach", method = RequestMethod.GET)
    public ResponseEntity<?> putOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                      @RequestHeader("X-Role") String role, @RequestParam("test") String test){
        if(role.equals("GUEST") || role.equals("EMPLOYEE"))
            return new ResponseEntity<>(test, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
