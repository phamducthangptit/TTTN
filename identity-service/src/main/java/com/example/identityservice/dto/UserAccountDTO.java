package com.example.identityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    private int roleId;
    private String userName;
    private String password;
    private int status;
    private String code;
}
