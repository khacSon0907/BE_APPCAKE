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
public class OrderRequest {

    private String userId;
    private List<OrderItem> items;
    private double totalPrice;           // Tổng giá trị đơn hàng
    private double discount;             // Tổng giảm giá của đơn (% hoặc giá trị tuỳ bạn)
    private String shippingAddress;      // Địa chỉ giao hàng
    private OrderStatus status;          // Trạng thái đơn hàng
    private PaymentMethod paymentMethod; // Phương thức thanh toán
    private LocalDateTime createdAt;     // Thời gian tạo đơn
    private LocalDateTime updatedAt;     // Thời gian cập nhật đơn (nếu có)
}
