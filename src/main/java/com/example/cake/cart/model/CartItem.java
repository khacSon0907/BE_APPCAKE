package com.example.cake.cart.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private String productId;
    private String name;
    private String image;
    private double price;
    private int quantity;
    private boolean selected;
    private int discount; // đơn vị %
}
