package com.makris.site.repositories;

import com.makris.site.entities.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

// Implementation will be automatically implemented by JpaRepository
public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findByCustomer(String customer);
}
