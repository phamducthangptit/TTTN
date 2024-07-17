package com.example.informationservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private int userId;
    private int accountId;
    private int status;
    private String roleName;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime createAt;
}
