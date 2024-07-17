package com.example.informationservice.controller;

import com.example.informationservice.dto.ChangePasswordDTO;
import com.example.informationservice.dto.ResetPasswordRequest;
import com.example.informationservice.dto.ResponseDTO;
import com.example.informationservice.service.AccountService;
import com.example.informationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/information-service/user")
public class UserController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                            @RequestHeader("X-Role") String role, @RequestBody ChangePasswordDTO changePasswordDTO) {
        int x = accountService.changePassword(changePasswordDTO);
        if (x == 1)
            return new ResponseEntity<>(new ResponseDTO("ChangePasswordOk", "Thay đổi mật khẩu thành công"), HttpStatus.OK);
        if(x == -1)
            return new ResponseEntity<>(new ResponseDTO("ErrorOldPassword", "Mật khẩu cũ không đúng"), HttpStatus.BAD_REQUEST);
        if(x == -2)
            return new ResponseEntity<>(new ResponseDTO("ErrorRepeatPassword", "Mật khẩu mới không được trùng với mật khẩu cũ"), HttpStatus.CONFLICT);
        return new ResponseEntity<>(new ResponseDTO("ErrorAccount", "UserName not found"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        int x = userService.resetPassword(resetPasswordRequest);
        if(x == 1)
            return new ResponseEntity<>(new ResponseDTO("resetPasswordOk", "Thay đổi mật khẩu thành công"), HttpStatus.OK);
        return new ResponseEntity<>(new ResponseDTO("resetPasswordFail", "Tên đăng nhập hoặc email không đúng"), HttpStatus.BAD_REQUEST);
    }
}
