package com.example.identityservice.controller;

import com.example.identityservice.code.GenerateCode;
import com.example.identityservice.dto.AuthRequest;
import com.example.identityservice.dto.ResponseDTO;
import com.example.identityservice.dto.UserAccountDTO;
import com.example.identityservice.entity.User;
import com.example.identityservice.service.AccountService;
import com.example.identityservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private HashMap<String, String> mapCode = new HashMap<>();

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome to security";
    }

    @PostMapping("/register")
    public ResponseEntity<?> createAccountAndUser(@RequestBody UserAccountDTO userAccountDTO) {
        int check = 0;
        System.out.println("mail dto: " + userAccountDTO.getEmail());
        System.out.println("code dto: " + userAccountDTO.getCode());
        for (String key : mapCode.keySet()) {
            String value = mapCode.get(key);
            System.out.println("mail: " + key);
            System.out.println("values: " + value);
            if (key.compareTo(userAccountDTO.getEmail()) == 0 && value.compareTo(userAccountDTO.getCode()) == 0) {
                check = 1;
                break;
            }
        }
        if (check == 1) {
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
        } else {
            ResponseDTO responseDTO = new ResponseDTO("ErrorCode", "Mã xác nhận không đúng");
            return new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> getToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                return new ResponseEntity<>(new ResponseDTO("token", accountService.generateToken(authRequest.getUsername())), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ResponseDTO("ErrorLogin", "Tên đăng nhập hoặc mật khẩu không đúng"), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        accountService.validateToken(token);
        return "token is validate";
    }

    @GetMapping("/send-code")
    public ResponseEntity<?> sendCode(@RequestParam("email") String email) {
        GenerateCode generateCode = new GenerateCode();
        String code = generateCode.generateCode();
        for (String tmp : mapCode.values()) { // check xem đã tồn tại code này chưa
            if (tmp.equals(code)) {
                code = generateCode.generateCode(); // tạo code khác nữa
                break;
            }
        }
        for (String key : mapCode.keySet()) {
            if (email.equals(key)) { // email này đã có mã gửi về
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        // nếu không nằm trong 2 t/h trên thì thỏa mãn điều kiện, cho vào map --> gửi mail
        mapCode.put(email, code);
        emailService.sendEmail(email, "Mã xác nhận", code);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
