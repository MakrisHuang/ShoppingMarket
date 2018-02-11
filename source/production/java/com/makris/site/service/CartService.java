package com.makris.site.service;

import com.makris.site.entities.Cart;
import com.makris.site.entities.ShoppingItem;
import com.makris.site.entities.UserPrincipal;

public interface CartService {
    Cart findCartByCustomer(UserPrincipal customer);
    void addItemToCart(UserPrincipal customer, ShoppingItem shoppingItem);
    void updateItemAmount(UserPrincipal customer,
                          ShoppingItem shoppingItem,
                          Integer newAmount);
    void removeItem(UserPrincipal customer,
                    ShoppingItem shoppingItem);
}
