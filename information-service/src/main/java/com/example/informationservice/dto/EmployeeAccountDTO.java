package com.example.informationservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAccountDTO {
    private String firstName;
    private String lastName;
    private String email;
    private int gender;
    private String phoneNumber;
    private String address;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime birthday;
    private int roleId;
    private String userName;
    private String password;
    private int status;
}
