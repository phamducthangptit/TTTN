package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDTO {
    private int ratting;
    private String userName;
    private int orderDetailId;
    private String comment;
    private LocalDateTime createAt;
    private int orderId;
}
