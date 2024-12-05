package com.example.informationservice.controller;

import com.example.informationservice.code.GenerateCode;
import com.example.informationservice.dto.ChangePasswordDTO;
import com.example.informationservice.dto.ResetPasswordRequest;
import com.example.informationservice.dto.ResponseDTO;
import com.example.informationservice.dto.UserDTO;
import com.example.informationservice.service.AccountService;
import com.example.informationservice.service.EmailService;
import com.example.informationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/information-service/user")
public class UserController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    private HashMap<String, String> mapCode = new HashMap<>();

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
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
        System.out.println(mapCode.size());
        System.out.println(resetPasswordRequest.getEmail());
        System.out.println(resetPasswordRequest.getCode());
        // check code
        int check = 0;
        for (String key : mapCode.keySet()) {
            String value = mapCode.get(key);
            if (key.compareTo(resetPasswordRequest.getEmail()) == 0 && value.compareTo(resetPasswordRequest.getCode()) == 0) {
                check = 1;
                break;
            }
        }
        if(check == 1){ // nhập đúng mã và email thì reset lại pasword
            int x = userService.resetPassword(resetPasswordRequest);
            if(x == 1){
                mapCode.remove(resetPasswordRequest.getEmail());
                return new ResponseEntity<>(new ResponseDTO("resetPasswordOk", "Đặt lại mật khẩu thành công"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ResponseDTO("resetPasswordFail", "Không tồn tại thông tin tài khoản"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseDTO("resetPasswordFail", "Email hoặc mã xác nhận không đúng"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get-information")
    public ResponseEntity<?> getInformation(@RequestHeader("X-Role") String role, @RequestParam("user-name") String userName){
        if(role.equals("GUEST"))
            return new ResponseEntity<>(userService.getInformationUser(userName), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/update-information")
    public ResponseEntity<?> updateInformation(@RequestHeader("X-Role") String role, @RequestBody UserDTO userDTO){
        if(role.equals("GUEST")){
            userService.updateInformation(userDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/send-code")
    public ResponseEntity<?> sendCode(@RequestParam("email") String email) {
        GenerateCode generateCode = new GenerateCode();
        String code;
        // Vòng lặp để tạo mã duy nhất
        do {
            code = generateCode.generateCode();
        } while (mapCode.containsValue(code));

        // nếu không nằm trong 2 t/h trên thì thỏa mãn điều kiện, cho vào map --> gửi mail
        mapCode.put(email, code);
        emailService.sendEmail(email, "Mã xác nhận để đặt lại mật khẩu", code);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
