package com.example.cake.favorite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteRequest {
    private String productId;
    private String name;
    private String image;
    private double price;
    private int quantity;
    private int discount; // đơn vị %
}

