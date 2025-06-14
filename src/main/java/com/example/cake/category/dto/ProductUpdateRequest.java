package com.example.cake.category.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class ProductUpdateRequest {
    private String id ;
    private String name ;
    private String description;
    private BigDecimal price;
    private String categoryCode; // Reference to Categories
    private String images;
    private Integer stock; // Số lượng tồn kho
    private String ingredients; // Thành phần
    private String size; // Kích thước (Small, Medium, Large)
    private Double weight; // Trọng lượng (gram)
    private Boolean isAvailable; // Còn bán hay không
    private LocalDateTime updatedAt;
}
