package com.example.cake.category.dto;

import lombok.Data;
import java.math.BigDecimal;
@Data
public class ProductCreateRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private String categoryCode;
    private Integer stock;
    private String ingredients;
    private String size;
    private Double weight;
    private Boolean isAvailable;
}
