package com.cfs.Ecommerce.service;

import com.cfs.Ecommerce.dto.OrderDTO;
import com.cfs.Ecommerce.dto.OrderItemDTO;
import com.cfs.Ecommerce.model.*;
import com.cfs.Ecommerce.repo.*;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private PaymentOrderRepo paymentOrderRepo;

    private RazorpayClient razorpayClient;

    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    @PostConstruct
    public void init() throws RazorpayException {
        this.razorpayClient = new RazorpayClient(razorpayKey, razorpaySecret);
    }

    @Transactional
    public OrderDTO placeOrder(Long userId, Map<Long, Integer> productQuantities, double totalAmount) throws RazorpayException {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // 1️⃣ Create new Order
        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());
        order.setAmount(totalAmount);

        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            Product product = productRepo.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(entry.getValue());
            orderItems.add(orderItem);

            orderItemDTOS.add(new OrderItemDTO(product.getName(), product.getPrice(), entry.getValue()));
        }

        order.setOrderItems(orderItems);

        // 2️⃣ Create Razorpay order
        JSONObject rzpRequest = new JSONObject();
        rzpRequest.put("amount", (int) (totalAmount * 100)); // Amount in paise
        rzpRequest.put("currency", "INR");
        rzpRequest.put("receipt", "txn_" + UUID.randomUUID());

        com.razorpay.Order rzpOrder = razorpayClient.orders.create(rzpRequest);

        // 3️⃣ Create PaymentOrder
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setUser(user);
        paymentOrder.setOrder(order);
        paymentOrder.setRazorpayOrderId(rzpOrder.get("id"));
        paymentOrder.setStatus("PENDING");
        paymentOrder.setCreatedAt(LocalDateTime.now());

        order.setPaymentOrder(paymentOrder);

        // 4️⃣ Save order (cascades to PaymentOrder & OrderItems)
        orderRepo.save(order);

        // 5️⃣ Convert to DTO
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getAmount());
        dto.setOrderDateTime(order.getOrderDate());
        dto.setOrderItems(orderItemDTOS);
        dto.setRazorpayOrderId(paymentOrder.getRazorpayOrderId());

        return dto;
    }

    @Transactional
    public void updateOrder(String razorpayOrderId, String razorpayPaymentId, String status) {
        PaymentOrder paymentOrder = paymentOrderRepo.findByRazorpayOrderId(razorpayOrderId);
        if (paymentOrder == null) throw new RuntimeException("PaymentOrder not found");

        paymentOrder.setPaymentId(razorpayPaymentId);
        paymentOrder.setStatus(status.trim().toUpperCase());
        paymentOrderRepo.save(paymentOrder);

        if ("SUCCESS".equalsIgnoreCase(status) || "PAID".equalsIgnoreCase(status) || "COMPLETED".equalsIgnoreCase(status)) {
            Order order = paymentOrder.getOrder();
            order.setStatus("SUCCESSFUL");
            orderRepo.save(order);
        }
    }

    public List<OrderDTO> findAllOrders() {
        return orderRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByUserId(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepo.findByUser(user).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private OrderDTO convertToDto(Order order) {
        List<OrderItemDTO> items = order.getOrderItems().stream()
                .map(oi -> new OrderItemDTO(
                        oi.getProduct().getName(),
                        oi.getProduct().getPrice(),
                        oi.getQuantity()
                ))
                .collect(Collectors.toList());

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserName(order.getUser().getName());
        dto.setEmail(order.getUser().getEmail());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getAmount());
        dto.setOrderDateTime(order.getOrderDate());
        dto.setOrderItems(items);

        if (order.getPaymentOrder() != null) {
            dto.setRazorpayOrderId(order.getPaymentOrder().getRazorpayOrderId());
        }

        return dto;
    }
}
