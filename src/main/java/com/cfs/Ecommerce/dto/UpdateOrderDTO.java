package com.cfs.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderDTO{
    private String razorpayOrderId;
    private String paymentId;
    private String status;
}
