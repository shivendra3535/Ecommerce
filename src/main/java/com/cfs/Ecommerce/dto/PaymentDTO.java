package com.cfs.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long id;

    private UserDTO user;

    private OrderDTO order;


}
