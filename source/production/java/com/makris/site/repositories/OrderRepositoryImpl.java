package com.makris.site.repositories;

import com.makris.site.entities.Order;
import com.makris.site.entities.UserPrincipal;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class OrderRepositoryImpl implements OrderTransactor{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public Order getLatestOrderOfCustomer(UserPrincipal customer) {
        LogManager.getLogger().info(customer.getId());
        String queryStr = "SELECT a FROM Order as a WHERE a.customer=:id ORDER BY a.id DESC";
        Order order = null;
        try {
            order = this.entityManager.createQuery(queryStr, Order.class)
                    .setParameter("id", customer).setMaxResults(1).getSingleResult();
        } catch (NoResultException e){
            e.printStackTrace();
        }
        if (order == null) {
            return null;
        }else{
            return order;
        }
    }
}
