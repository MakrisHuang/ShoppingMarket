package com.makris.site.service;

import com.makris.site.entities.Cart;
import com.makris.site.entities.CartItem;
import com.makris.site.entities.ShoppingItem;
import com.makris.site.entities.UserPrincipal;

public interface CartService {
    Cart findCartByCustomer(UserPrincipal customer);
    Cart addItemToCart(Cart cart, ShoppingItem shoppingItem);
    Cart updateItemAmount(Cart cart, CartItem newCartItem);
    Cart deleteItem(Cart cart,
                    ShoppingItem shoppingItem);
    void deleteCartByCustomer(UserPrincipal customer);
}
