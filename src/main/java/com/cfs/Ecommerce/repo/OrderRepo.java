package com.cfs.Ecommerce.repo;

import com.cfs.Ecommerce.model.Order;
import com.cfs.Ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

}
