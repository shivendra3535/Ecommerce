package com.cfs.Ecommerce.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private Long userId;
    private Map<Long, Integer> productQuantities;

    private double totalAmount;

}
