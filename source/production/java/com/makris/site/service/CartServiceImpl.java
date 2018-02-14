package com.makris.site.service;

import com.makris.site.entities.Cart;
import com.makris.site.entities.CartItem;
import com.makris.site.entities.ShoppingItem;
import com.makris.site.entities.UserPrincipal;
import com.makris.site.repositories.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
public class CartServiceImpl implements CartService{
    @Inject CartRepository cartRepository;

    @Override
    @Transactional
    public Cart findCartByCustomer(UserPrincipal customer) {
        return this.cartRepository.findByCustomer(customer);
    }

    @Override
    @Transactional
    public Cart addItemToCart(Cart cart,
                              ShoppingItem shoppingItem) {
        CartItem cartItem = new CartItem();
        cartItem.setShoppingItem(shoppingItem);
        cartItem.setAmount(1);

        cart.addCartItem(cartItem);
        this.cartRepository.save(cart);

        return cart;
    }

    @Override
    @Transactional
    public Cart updateItemAmount(Cart cart,
                                 ShoppingItem shoppingItem,
                                 Integer newAmount) {
        CartItem cartItem = new CartItem();
        cartItem.setShoppingItem(shoppingItem);

        cart.updateCartItemAmount(cartItem, newAmount);
        this.cartRepository.save(cart);

        return cart;
    }

    @Override
    @Transactional
    public Cart removeItem(Cart cart,
                           ShoppingItem shoppingItem) {
        CartItem cartItem = new CartItem();
        cartItem.setShoppingItem(shoppingItem);

        cart.removeCartItem(cartItem);
        this.cartRepository.save(cart);

        return cart;
    }
}
