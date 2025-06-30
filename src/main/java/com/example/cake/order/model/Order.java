package com.example.cake.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    private String id;                   // Order ID

    private String userId;               // ID của user đặt đơn
    private List<OrderItem> items;       // Danh sách sản phẩm trong đơn

    private double totalPrice;           // Tổng giá trị đơn hàng
    private double shippingFee;          // Phí vận chuyển
    private double discount;             // Tổng giảm giá của đơn (% hoặc giá trị tuỳ bạn)

    private String shippingAddress;      // Địa chỉ giao hàng
    private OrderStatus status;          // Trạng thái đơn hàng
    private PaymentMethod paymentMethod; // Phương thức thanh toán

    private LocalDateTime createdAt;     // Thời gian tạo đơn
    private LocalDateTime updatedAt;     // Thời gian cập nhật đơn (nếu có)
}
