package com.example.cake.favorite.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteItem {

    private String productId;
    private String name;
    private String image;
    private double price;
    private int quantity;
    private boolean selected;
    private int discount; // đơn vị %
}
