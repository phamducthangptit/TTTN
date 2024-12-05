package com.example.informationservice.controller;

import com.example.informationservice.dto.*;
import com.example.informationservice.entity.Staff;
import com.example.informationservice.service.AccountService;
import com.example.informationservice.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/information-service/admin")
public class AdminController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private StaffService staffService;

    @GetMapping("/list-employee")
    public ResponseEntity<?> listEmployee(@RequestHeader("X-Role") String role) {
        if (role.equals("ADMIN")) {
            List<StaffDTO> list = staffService.getAllStaff();
            if (list.isEmpty()) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/update-status-account")
    public ResponseEntity<?> updateStatusAccount(@RequestHeader("X-Role") String role, @RequestBody UpdateStatusRequest updateStatusRequest) {
        if (role.equals("ADMIN") || role.equals("EMPLOYEE")) {
            accountService.updateStatusAccount(updateStatusRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/add-employee")
    public ResponseEntity<?> addEmployee(@RequestHeader("X-Role") String role, @RequestBody EmployeeAccountDTO employeeAccountDTO) {
        if(role.equals("ADMIN")){
            Staff staff = new Staff();
            staff.setFirstName(employeeAccountDTO.getFirstName());
            staff.setLastName(employeeAccountDTO.getLastName());
            staff.setEmail(employeeAccountDTO.getEmail());
            staff.setPhoneNumber(employeeAccountDTO.getPhoneNumber());
            staff.setGender(employeeAccountDTO.getGender());
            staff.setAddress(employeeAccountDTO.getAddress());
            staff.setBirthday(employeeAccountDTO.getBirthday());
            staff.setCreateAt(LocalDateTime.now());
            staff.setUserName(employeeAccountDTO.getUserName());

            int result = accountService.createAccountAndStaff(
                    staff,
                    employeeAccountDTO.getRoleId(),
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
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    // get information employee, admin
    @GetMapping("/get-information")
    public ResponseEntity<?> getInformation(@RequestHeader("X-Role") String role, @RequestParam("user-name") String userName) {
        if (role.equals("ADMIN") || role.equals("EMPLOYEE")) {
            return new ResponseEntity<>(staffService.getInformationStaff(userName), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    // update information employee, admin
    @PostMapping("/update-information")
    public ResponseEntity<?> updateInformation(@RequestHeader("X-Role") String role, @RequestBody StaffDTO staffDTO) {
        if (role.equals("ADMIN") || role.equals("EMPLOYEE")) {
            staffService.updateInformation(staffDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
