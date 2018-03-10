package com.makris.site.repositories;

import com.makris.site.entities.Order;
import com.makris.site.entities.UserPrincipal;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

// Implementation will be automatically implemented by JpaRepository
public interface OrderRepository extends CrudRepository<Order, Long>, OrderTransactor {
    List<Order> findByCustomer(UserPrincipal principal);
}
