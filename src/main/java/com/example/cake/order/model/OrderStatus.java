package com.example.cake.order.model;

public enum OrderStatus {
    PENDING,        // Chờ xác nhận
    CONFIRMED,      // Đã xác nhận
    SHIPPING,       // Đang giao
    COMPLETED,      // Hoàn thành
    CANCELLED       // Bị hủy
}
