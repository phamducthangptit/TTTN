package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateDTO {
    private int categoryId;
    private String name;
    private List<DetailResponseDTO> categoryDetails;

    @Override
    public String toString() {
        return "CategoryUpdateDTO{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", categoryDetails=" + categoryDetails +
                '}';
    }
}
