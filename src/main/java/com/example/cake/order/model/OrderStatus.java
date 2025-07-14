package com.example.cake.order.model;

public enum OrderStatus {
    PENDING,
    PROCESSING,// Chờ xác nhận
    CONFIRMED,      // Đã xác nhận
    CANCELLED       // Bị hủy
}
