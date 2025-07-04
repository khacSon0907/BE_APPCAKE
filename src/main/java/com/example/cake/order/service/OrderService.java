package com.example.cake.order.service;

import com.example.cake.auth.repository.UserRepository;
import com.example.cake.cart.model.CartItem;
import com.example.cake.cart.service.CartSerivce;
import com.example.cake.order.dto.OrderRequest;
import com.example.cake.order.model.Order;
import com.example.cake.order.model.OrderItem;
import com.example.cake.order.model.OrderStatus;
import com.example.cake.order.model.PaymentMethod;
import com.example.cake.order.respository.OrderRepository;
import com.example.cake.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final CartSerivce  cartSerivce;

    public ResponseMessage<Order> createOrder(Order orderRequest) {
        try {
            // Validate order data
            if (orderRequest == null) {
                return new ResponseMessage<>(false, "Order data is required", null);
            }

            if (orderRequest.getUserId().isEmpty()) {
                return new ResponseMessage<>(false, "User ID is required", null);
            }

            if (orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
                return new ResponseMessage<>(false, "Order items are required", null);
            }

            if (orderRequest.getShippingAddress() == null || orderRequest.getShippingAddress().trim().isEmpty()) {
                return new ResponseMessage<>(false, "Shipping address is required", null);
            }

            // Validate payment method
            if (orderRequest.getPaymentMethod() == null) {
                return new ResponseMessage<>(false, "Payment method is required", null);
            }

            // Create Order entity from OrderRequest
            Order order = Order.builder()
                    .userId(orderRequest.getUserId())
                    .items(orderRequest.getItems())
                    .discount(orderRequest.getDiscount())
                    .shippingAddress(orderRequest.getShippingAddress())
                    .paymentMethod(orderRequest.getPaymentMethod())
                    .status(OrderStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            // Calculate total price if not provided
            if (orderRequest.getTotalPrice() <= 0) {
                double calculatedTotal = calculateTotalPrice(order);
                order.setTotalPrice(calculatedTotal);
            } else {
                order.setTotalPrice(orderRequest.getTotalPrice());
            }

            Order savedOrder = orderRepository.save(order);

            // Extract product IDs from order items to remove from cart
            List<String> productIds = orderRequest.getItems().stream()
                    .map(OrderItem::getProductId)
                    .toList();
            
            if (cartSerivce.deleteCartItemsByProductIds(orderRequest.getUserId(), productIds)) {
                // Log warning but don't fail the order creation
                System.out.println("Warning: Failed to remove items from cart after order creation");
            }

            return new ResponseMessage<>(true, "Order created successfully", savedOrder);
        } catch (Exception e) {
            return new ResponseMessage<>(false, "Failed to create order: " + e.getMessage(), null);
        }
    }

    public ResponseMessage<List<Order>> getAllOrders() {
        try {
            List<Order> orders = orderRepository.findAll();
            return new ResponseMessage<>(true, "Orders retrieved successfully", orders);
        } catch (Exception e) {
            return new ResponseMessage<>(false, "Failed to retrieve orders: " + e.getMessage(), null);
        }
    }
    public ResponseMessage<Order> getOrderById(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            return new ResponseMessage<>(false, "Order ID is required", null);
        }

        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            return new ResponseMessage<>(true, "Order retrieved successfully", orderOptional.get());
        }
        return new ResponseMessage<>(false, "Order not found with ID: " + orderId, null);
    }

    public ResponseMessage<List<Order>> getOrdersByUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return new ResponseMessage<>(false, "User ID is required", null);
        }

        try {
            List<Order> orders = orderRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
            return new ResponseMessage<>(true, "Orders retrieved successfully", orders);
        } catch (Exception e) {
            return new ResponseMessage<>(false, "Failed to retrieve orders: " + e.getMessage(), null);
        }
    }

    public ResponseMessage<Order> updateOrderStatus(String orderId, OrderStatus status) {
        if (orderId == null || orderId.trim().isEmpty()) {
            return new ResponseMessage<>(false, "Order ID is required", null);
        }
        
        if (status == null) {
            return new ResponseMessage<>(false, "Order status is required", null);
        }

        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            return new ResponseMessage<>(false, "Order not found with ID: " + orderId, null);
        }

        Order order = orderOptional.get();
        
        // Business logic for status transitions
        if (order.getStatus() == OrderStatus.CANCELLED) {
            return new ResponseMessage<>(false, "Cannot update status of cancelled order", null);
        }

        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());

        try {
            Order updatedOrder = orderRepository.save(order);
            return new ResponseMessage<>(true, "Order status updated successfully", updatedOrder);
        } catch (Exception e) {
            return new ResponseMessage<>(false, "Failed to update order status: " + e.getMessage(), null);
        }
    }

    public ResponseMessage<String> cancelOrder(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            return new ResponseMessage<>(false, "Order ID is required", null);
        }

        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            return new ResponseMessage<>(false, "Order not found with ID: " + orderId, null);
        }

        Order order = orderOptional.get();
        
        // Only allow cancellation for PENDING orders
        if (order.getStatus() == OrderStatus.CONFIRMED) {
            return new ResponseMessage<>(false, "Cannot cancel confirmed order", null);
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            return new ResponseMessage<>(false, "Order is already cancelled", null);
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());

        try {
            orderRepository.save(order);
            return new ResponseMessage<>(true, "Order cancelled successfully", null);
        } catch (Exception e) {
            return new ResponseMessage<>(false, "Failed to cancel order: " + e.getMessage(), null);
        }
    }

    public ResponseMessage<List<Order>> getOrdersByStatus(OrderStatus status) {
        if (status == null) {
            return new ResponseMessage<>(false, "Order status is required", null);
        }

        try {
            List<Order> orders = orderRepository.findAllByStatus(status);
            return new ResponseMessage<>(true, "Orders retrieved successfully", orders);
        } catch (Exception e) {
            return new ResponseMessage<>(false, "Failed to retrieve orders: " + e.getMessage(), null);
        }
    }

    public ResponseMessage<List<Order>> getOrdersByPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            return new ResponseMessage<>(false, "Payment method is required", null);
        }

        try {
            List<Order> orders = orderRepository.findAllByPaymentMethod(paymentMethod);
            return new ResponseMessage<>(true, "Orders retrieved successfully", orders);
        } catch (Exception e) {
            return new ResponseMessage<>(false, "Failed to retrieve orders: " + e.getMessage(), null);
        }
    }

    public ResponseMessage<Double> calculateTotalRevenue() {
        try {
            List<Order> confirmedOrders = orderRepository.findAllByStatus(OrderStatus.CONFIRMED);
            double totalRevenue = confirmedOrders.stream()
                    .mapToDouble(Order::getTotalPrice)
                    .sum();
            return new ResponseMessage<>(true, "Total revenue calculated successfully", totalRevenue);
        } catch (Exception e) {
            return new ResponseMessage<>(false, "Failed to calculate total revenue: " + e.getMessage(), null);
        }
    }

    private double calculateTotalPrice(Order order) {
        double itemsTotal = order.getItems().stream()
                .mapToDouble(item -> {
                    double itemPrice = item.getPrice() * item.getQuantity();
                    double discountAmount = itemPrice * (item.getDiscount() / 100.0);
                    return itemPrice - discountAmount;
                })
                .sum();

        // Apply order-level discount
        double discountAmount = itemsTotal * (order.getDiscount() / 100.0);
        itemsTotal -= discountAmount;
        
        return Math.max(0, itemsTotal); // Ensure total is not negative
    }
}
