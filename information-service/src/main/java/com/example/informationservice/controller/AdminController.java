package com.example.informationservice.controller;

import com.example.informationservice.dto.*;
import com.example.informationservice.entity.User;
import com.example.informationservice.service.AccountService;
import com.example.informationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/information/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/list-employee")
    public ResponseEntity<?> listEmployee(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                          @RequestHeader("X-Role") String role) {
        if (role.equals("ADMIN")) {
            List<EmployeeDTO> list = userService.getAllUsers();
            if (list.isEmpty()) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/update-status-account")
    public ResponseEntity<?> updateStatusAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                 @RequestHeader("X-Role") String role, @RequestBody UpdateStatusRequest updateStatusRequest) {
        if (role.equals("ADMIN")) {
            accountService.updateStatusAccount(updateStatusRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/update-information-employee")
    public ResponseEntity<?> updateInformationEmployee(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                       @RequestHeader("X-Role") String role, @RequestBody UpdateEmployeeRequest updateEmployeeRequest) {
        if (role.equals("ADMIN")) {
            int result = userService.updateEmployee(updateEmployeeRequest);
            if (result == -1) {
                return new ResponseEntity<>(new ResponseDTO("ErrorEmail", "Đã tồn tại email này!"), HttpStatus.BAD_REQUEST);
            } else
                return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/add-employee")
    public ResponseEntity<?> addEmployee(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                         @RequestHeader("X-Role") String role, @RequestBody EmployeeAccountDTO employeeAccountDTO) {
        User user = new User();
        user.setFirstName(employeeAccountDTO.getFirstName());
        user.setLastName(employeeAccountDTO.getLastName());
        user.setEmail(employeeAccountDTO.getEmail());
        user.setAddress(employeeAccountDTO.getAddress());
        user.setPhoneNumber(employeeAccountDTO.getPhoneNumber());
        user.setCreateAt(LocalDateTime.now());

        int result = accountService.createAccountAndUser(
                user,
                employeeAccountDTO.getRoleId(),
                employeeAccountDTO.getUserName(),
                employeeAccountDTO.getPassword(),
                employeeAccountDTO.getStatus()
        );
        if (result == -1) {
            ResponseDTO responseDTO = new ResponseDTO("ErrorEmail", "Email đã tồn tại");
            return new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
        }
        if (result == -2) {
            ResponseDTO responseDTO = new ResponseDTO("ErrorUserName", "UserName đã tồn tại");
            return new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
        }
        ResponseDTO responseDTO = new ResponseDTO("CreateAccountOk", "Tạo tài khoản thành công");
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
