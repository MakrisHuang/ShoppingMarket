package com.makris.site.endpoint;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.makris.config.annotation.RestEndpoint;
import com.makris.site.entities.Cart;
import com.makris.site.entities.CartItem;
import com.makris.site.entities.ShoppingItem;
import com.makris.site.entities.UserPrincipal;
import com.makris.site.security.JwtUtils;
import com.makris.site.service.CartService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestEndpoint
@ResponseBody
public class CartRestEndPoint {

    @Inject
    CartService cartService;
    @Inject
    JwtUtils jwtUtils;

    private static final Logger logger = LogManager.getLogger();

    @RequestMapping(value = "cart/viewall", method = RequestMethod.POST)
    public Cart viewCart(@RequestBody Token token){
        UserPrincipal customer = jwtUtils.getUserFromToken(token.getToken(), false);
        if (customer != null){
            return this.cartService.findCartByCustomer(customer);
        }
        return null;
    }

    @RequestMapping(value = "cart/add", method = RequestMethod.POST)
    public Cart addItemToCart(@RequestBody Token token){

        if (customer != null){
            Cart cart = this.cartService.findCartByCustomer(customer);
            if (cart == null) cart = new Cart();

            cart.setCustomer(customer);
            cart = this.cartService.addItemToCart(cart, shoppingItem);
            return cart;
        }else {

            return null;
        }
    }

    @RequestMapping(value = "cart/update", method = RequestMethod.POST)
    public Cart updateItemInCart(@RequestBody CartItem newCartItem,
                           HttpServletRequest request){
        HttpSession session = request.getSession();
        UserPrincipal customer = (UserPrincipal)session.getAttribute(UserPrincipal.SESSION_ATTRIBUTE_KEY);

        logger.info("[cart/update] cartItem: ");
        logger.info(newCartItem.toString());

        if (customer != null){
            Cart cart = this.cartService.findCartByCustomer(customer);
            cart = cartService.updateItemAmount(cart, newCartItem);
            return cart;
        }else{
            return null;
        }
    }

    @RequestMapping(value = "cart/delete", method = RequestMethod.POST)
    public Cart deleteItemInCart(@RequestBody ShoppingItem shoppingItem,
                                 HttpServletRequest request){
        HttpSession session = request.getSession();
        UserPrincipal customer = (UserPrincipal)session.getAttribute(UserPrincipal.SESSION_ATTRIBUTE_KEY);

        logger.info("[cart/delete] shoppingItem: ");
        logger.info(shoppingItem);

        if (customer != null){
            Cart cart = this.cartService.findCartByCustomer(customer);
            cart = this.cartService.deleteItem(cart, shoppingItem);
            return cart;
        }else{
            return null;
        }
    }

    @JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE,
            fieldVisibility = JsonAutoDetect.Visibility.NONE,
            getterVisibility = JsonAutoDetect.Visibility.NONE,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE,
            setterVisibility = JsonAutoDetect.Visibility.NONE)
    public static class Token{
        private String token;

        @JsonProperty
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
