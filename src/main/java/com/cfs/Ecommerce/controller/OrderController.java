package com.cfs.Ecommerce.controller;

import com.cfs.Ecommerce.dto.OrderDTO;
import com.cfs.Ecommerce.dto.UpdateOrderDTO;
import com.cfs.Ecommerce.model.OrderRequest;
import com.cfs.Ecommerce.service.OrderService;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Place a new order
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest request) {
        try {
            OrderDTO orderDTO = orderService.placeOrder(
                    request.getUserId(),
                    request.getProductQuantities(),
                    request.getTotalAmount()
            );
            return ResponseEntity.ok(orderDTO);
        } catch (RazorpayException e) {
            return ResponseEntity.status(500).body("Razorpay error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Order placement failed: " + e.getMessage());
        }
    }

    // Update order after payment
    @PostMapping("/update-order")
    public ResponseEntity<?> updateOrder(@RequestBody UpdateOrderDTO dto) {
        try {
            orderService.updateOrder(
                    dto.getRazorpayOrderId(),
                    dto.getPaymentId(),
                    dto.getStatus()
            );
            return ResponseEntity.ok("Order updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update order: " + e.getMessage());
        }
    }

    // Admin: fetch all orders
    @GetMapping("/admin/all")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAllOrders());
    }

    // User: fetch orders by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }
}
