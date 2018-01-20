package com.makris.site.service;

import com.makris.site.entities.Order;
import com.makris.site.entities.ShoppingItem;
import com.makris.site.repositories.OrderRepository;
import com.makris.site.repositories.ShoppingItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    @Inject OrderRepository orderRepository;
//    @Inject ShoppingItemRepository shoppingItemRepository;

    @Override
    @Transactional
    public List<Order> getAllOrdersByCustomer(String customer) {
        Iterable<Order> orders = this.orderRepository.findByCustomer(customer);
        List<Order> orderList = new ArrayList<>();
        orders.forEach(orderList::add);
        return orderList;
    }

    @Override
    @Transactional
    public Order getOrder(long orderId) {
        return this.orderRepository.findOne(orderId);
    }

    @Override
    @Transactional
    public void save(Order order) {
        this.orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(long orderId) {
        this.orderRepository.delete(orderId);
    }

    @Override
    @Transactional
    public Page<ShoppingItem> getShoppingItems(long orderId, Pageable page) {
//        return this.shoppingItemRepository.findByOrderId(orderId, page);
        return null;
    }

    @Override
    @Transactional
    public void saveShoppingItem(ShoppingItem shoppingItem, long orderId) {
//        this.shoppingItemRepository.save(shoppingItem);
    }

    @Override
    @Transactional
    public void deleteShoppingItem(long shoppingItemId) {
//        this.shoppingItemRepository.delete(shoppingItemId);
    }
}
