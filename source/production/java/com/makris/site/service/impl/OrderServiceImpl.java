package com.makris.site.service.impl;

import com.makris.site.entities.Order;
import com.makris.site.entities.UserPrincipal;
import com.makris.site.repositories.OrderRepository;
import com.makris.site.repositories.ShoppingItemRepository;
import com.makris.site.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject OrderRepository orderRepository;
    @Inject
    ShoppingItemRepository shoppingItemRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public List<Order> getAllOrdersByCustomer(UserPrincipal customer) {
        Iterable<Order> orders = this.orderRepository.findByCustomer(customer);
        List<Order> orderList = new ArrayList<>();
        orders.forEach(orderList::add);
        return orderList;
    }

    @Override
    public Order getLatestOrder(UserPrincipal customer) {
        return this.orderRepository.getLatestOrderOfCustomer(customer);
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
}
