package com.cfs.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;

    private double totalAmount;

    private String status;

    private LocalDateTime orderDateTime;

    private String userName;

    private String email;

    private List<OrderItemDTO> orderItems;

    private String razorpayOrderId;

}
