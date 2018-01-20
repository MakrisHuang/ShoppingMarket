package com.makris.site.service;

import com.makris.site.entities.Order;
import com.makris.site.entities.ShoppingItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface OrderService {
    @NotNull
    List<Order> getAllOrdersByCustomer(String customer);

    Order getOrder(long orderId);
    void save(@Valid Order order);
    void deleteOrder(long orderId);

    Page<ShoppingItem> getShoppingItems(long orderId, Pageable page);
    void saveShoppingItem(@Valid ShoppingItem shoppingItem, long orderId);
    void deleteShoppingItem(long shoppingItemId);
}
