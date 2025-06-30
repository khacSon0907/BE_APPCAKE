package com.example.cake.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    private String productId;
    private String name;
    private String image;
    private double price;     // Giá chốt lúc đặt hàng
    private int quantity;
    private int discount;     // Giảm giá đơn vị %
}
