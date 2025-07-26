package com.example.cake.order.service;

import com.example.cake.auth.model.User;
import com.example.cake.auth.repository.UserRepository;
import com.example.cake.cart.model.CartItem;
import com.example.cake.cart.service.CartSerivce;
import com.example.cake.category.model.Product;
import com.example.cake.category.repository.ProductRepository;
import com.example.cake.order.dto.OrderRequest;
import com.example.cake.order.dto.OrderResponse;
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
    private final UserRepository userRepository;
    private final CartSerivce  cartSerivce;
    private final ProductRepository productRepository;

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
                    .status(OrderStatus.UNCONFIRMED)
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

    public ResponseMessage<List<OrderResponse>> getAllOrders() {
        try {
            List<Order> orders = orderRepository.findAll();
            
            // Convert Order to OrderResponse with user info
            List<OrderResponse> orderResponses = orders.stream()
                    .map(order -> {
                        // Get user information for each order
                        Optional<User> userOptional = userRepository.findById(order.getUserId());
                        String userName = userOptional.map(User::getFullname).orElse("Unknown User");
                        String userPhone = userOptional.map(User::getPhoneNumber).orElse("Unknown Phone");
                        
                        return OrderResponse.builder()
                                .id(order.getId())
                                .userId(order.getUserId())
                                .userName(userName)
                                .userPhone(userPhone)
                                .items(order.getItems())
                                .totalPrice(order.getTotalPrice())
                                .discount(order.getDiscount())
                                .shippingAddress(order.getShippingAddress())
                                .paymentMethod(order.getPaymentMethod())
                                .status(order.getStatus())
                                .createdAt(order.getCreatedAt())
                                .updatedAt(order.getUpdatedAt())
                                .build();
                    })
                    .toList();
            
            return new ResponseMessage<>(true, "Orders retrieved successfully", orderResponses);
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

    public ResponseMessage<List<OrderResponse>> getOrdersByUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return new ResponseMessage<>(false, "User ID is required", null);
        }

        try {
            // Get user information
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                return new ResponseMessage<>(false, "User not found with ID: " + userId, null);
            }
            
            User user = userOptional.get();
            List<Order> orders = orderRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
            
            // Convert Order to OrderResponse with user info
            List<OrderResponse> orderResponses = orders.stream()
                    .map(order -> OrderResponse.builder()
                            .id(order.getId())
                            .userId(order.getUserId())
                            .userName(user.getFullname())
                            .userPhone(user.getPhoneNumber())
                            .items(order.getItems())
                            .totalPrice(order.getTotalPrice())
                            .discount(order.getDiscount())
                            .shippingAddress(order.getShippingAddress())
                            .paymentMethod(order.getPaymentMethod())
                            .status(order.getStatus())
                            .createdAt(order.getCreatedAt())
                            .updatedAt(order.getUpdatedAt())
                            .build())
                    .toList();
            
            return new ResponseMessage<>(true, "Orders retrieved successfully", orderResponses);
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
        OrderStatus previousStatus = order.getStatus();
        
        // Business logic for status transitions
        if (order.getStatus() == OrderStatus.CANCELLED) {
            return new ResponseMessage<>(false, "Cannot update status of cancelled order", null);
        }

        // Check if we're confirming the order and need to update stock
        if (status == OrderStatus.CONFIRMED && previousStatus != OrderStatus.CONFIRMED) {
            ResponseMessage<String> stockUpdateResult = updateProductStock(order);
            if (!stockUpdateResult.isSuccess()) {
                return new ResponseMessage<>(false, stockUpdateResult.getMessage(), null);
            }
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

    private ResponseMessage<String> updateProductStock(Order order) {
        try {
            for (OrderItem item : order.getItems()) {
                Optional<Product> productOptional = productRepository.findById(item.getProductId());
                
                if (productOptional.isEmpty()) {
                    return new ResponseMessage<>(false, 
                        "Product not found with ID: " + item.getProductId(), null);
                }
                
                Product product = productOptional.get();
                
                // Check if there's enough stock
                if (product.getStock() == null || product.getStock() < item.getQuantity()) {
                    return new ResponseMessage<>(false, 
                        "Insufficient stock for product: " + product.getName() + 
                        ". Available: " + (product.getStock() != null ? product.getStock() : 0) + 
                        ", Required: " + item.getQuantity(), null);
                }
                
                // Decrease stock
                product.setStock(product.getStock() - item.getQuantity());
                product.setUpdatedAt(LocalDateTime.now());
                
                // Update availability if stock reaches 0
                if (product.getStock() <= 0) {
                    product.setIsAvailable(false);
                }
                
                productRepository.save(product);
            }
            
            return new ResponseMessage<>(true, "Product stock updated successfully", null);
        } catch (Exception e) {
            return new ResponseMessage<>(false, 
                "Failed to update product stock: " + e.getMessage(), null);
        }
    }
}
