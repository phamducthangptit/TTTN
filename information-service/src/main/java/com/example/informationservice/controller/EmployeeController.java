package com.example.informationservice.controller;

import com.example.informationservice.dto.GuestDTO;
import com.example.informationservice.service.AccountService;
import com.example.informationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/information-service/employee")
public class EmployeeController {
    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/list-guest")
    public ResponseEntity<?> listEmployee(@RequestHeader("X-Role") String role,
                                          @RequestParam int page,
                                          @RequestParam int size) {
        if (role.equals("EMPLOYEE")) {
            Pageable pageable = PageRequest.of(page, size);
            Page<GuestDTO> list = userService.getAllGuest(pageable);
            if (list.isEmpty()) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
