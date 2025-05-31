package com.example.cake.category.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CategoryCreateRequest {

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;
}
