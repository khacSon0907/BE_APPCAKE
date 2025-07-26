package com.example.cake.order.dto;

import com.example.cake.order.model.OrderItem;
import com.example.cake.order.model.OrderStatus;
import com.example.cake.order.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private String id;
    private String userId;
    private String userName;        // Tên user
    private String userPhone;       // Số điện thoại user
    private List<OrderItem> items;
    private double totalPrice;
    private double discount;
    private String shippingAddress;
    private PaymentMethod paymentMethod;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}