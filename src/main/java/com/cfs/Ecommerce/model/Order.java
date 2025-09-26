package com.cfs.Ecommerce.model;

import com.cfs.Ecommerce.model.OrderItem;
import com.cfs.Ecommerce.model.PaymentOrder;
import com.cfs.Ecommerce.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    private LocalDateTime orderDate;

    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    // Do NOT have payment_order_id here
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private PaymentOrder paymentOrder;
}
