package com.makris.site.service.impl;

import com.makris.site.entities.Cart;
import com.makris.site.entities.CartItem;
import com.makris.site.entities.ShoppingItem;
import com.makris.site.entities.UserPrincipal;
import com.makris.site.repositories.CartRepository;
import com.makris.site.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
public class CartServiceImpl implements CartService {
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
        if (cart.shoppingItemIsInCart(shoppingItem)){
            // then increase its amount with 1
            CartItem cartItem = cart.findCartItemByShoppingItem(shoppingItem);
            Integer amount = cartItem.getAmount();
            amount += 1;
            cartItem.setAmount(amount);
            cart.updateCartItemAmount(cartItem);
        }else{
            // new item
            CartItem cartItem = new CartItem();
            cartItem.setShoppingItem(shoppingItem);
            cartItem.setAmount(1);

            cart.addCartItem(cartItem);
        }
        this.cartRepository.save(cart);
        return cart;
    }

    @Override
    @Transactional
    public Cart updateItemAmount(Cart cart,
                                 CartItem newCartItem) {
        cart.updateCartItemAmount(newCartItem);
        this.cartRepository.save(cart);

        return cart;
    }

    @Override
    @Transactional
    public Cart deleteItem(Cart cart,
                           ShoppingItem shoppingItem) {
        cart.removeCartItem(shoppingItem);
        this.cartRepository.save(cart);

        if (cart.getCartItems().size() == 0) {
            this.cartRepository.delete(cart);
        }

        return cart;
    }
}
