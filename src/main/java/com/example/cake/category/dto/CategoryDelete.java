package com.example.cake.category.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDelete {
    @NotBlank(message = "Code is required")
    private String code ;
}
