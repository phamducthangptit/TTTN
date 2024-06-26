package com.example.identityservice.controller;

import com.example.identityservice.dto.AuthRequest;
import com.example.identityservice.dto.UserAccountDTO;
import com.example.identityservice.entity.Account;
import com.example.identityservice.entity.User;
import com.example.identityservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome(){
        return "welcome to security";
    }

    @PostMapping("/register")
    public ResponseEntity<?> createAccountAndUser(@RequestBody UserAccountDTO userAccountDTO) {
        User user = new User();

        user.setFirstName(userAccountDTO.getFirstName());
        user.setLastName(userAccountDTO.getLastName());
        user.setEmail(userAccountDTO.getEmail());
        user.setAddress(userAccountDTO.getAddress());
        user.setPhoneNumber(userAccountDTO.getPhoneNumber());
        user.setCreateAt(LocalDateTime.now());

        int result = accountService.createAccountAndUser(
                user,
                userAccountDTO.getRoleId(),
                userAccountDTO.getUserName(),
                userAccountDTO.getPassword(),
                userAccountDTO.getStatus()
        );
        if(result == -1){
            return ResponseEntity.badRequest().body("Email đã tồn tại");
        }
        if(result == -2){
            return ResponseEntity.badRequest().body("UserName đã tồn tại");
        }
        return ResponseEntity.ok().body("Tạo tài khoản thành công");
    }

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return accountService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request");
        }
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        accountService.validateToken(token);
        return "token is validate";
    }
}
