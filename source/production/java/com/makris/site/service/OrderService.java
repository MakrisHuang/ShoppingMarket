package com.makris.site.service;

import com.makris.site.entities.Order;
import com.makris.site.entities.UserPrincipal;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface OrderService {
    @NotNull
    List<Order> getAllOrdersByCustomer(UserPrincipal customer);

    Order getOrder(long orderId);
    void save(@Valid Order order);
    void deleteOrder(long orderId);
}
