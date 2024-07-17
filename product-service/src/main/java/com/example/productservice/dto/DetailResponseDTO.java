package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailResponseDTO {
    private int detailId;
    private String name;

    @Override
    public String toString() {
        return "DetailResponseDTO{" +
                "detailId=" + detailId +
                ", name='" + name + '\'' +
                '}';
    }
}
