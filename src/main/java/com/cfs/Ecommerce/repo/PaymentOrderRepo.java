package com.cfs.Ecommerce.repo;

import com.cfs.Ecommerce.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentOrderRepo extends JpaRepository<PaymentOrder, Long> {
    PaymentOrder findByRazorpayOrderId(String id);
}
