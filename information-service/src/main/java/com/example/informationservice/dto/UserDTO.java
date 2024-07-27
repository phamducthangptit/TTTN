package com.example.informationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
}
