package com.example.cake.order.model;

public enum OrderStatus {
    UNCONFIRMED,    // Chưa xác nhận
    CONFIRMED,      // Đã xác nhận
    SHIPPING,       // Đang giao
    COMPLETED,      // Hoàn tất
    CANCELLED       // Đã hủy
}
