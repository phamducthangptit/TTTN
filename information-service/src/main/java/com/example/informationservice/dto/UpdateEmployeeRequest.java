package com.example.informationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeRequest {
    private int id;
    private String lastName;
    private String firstName;
    private String email;
    private String address;
    private String phoneNumber;
}
