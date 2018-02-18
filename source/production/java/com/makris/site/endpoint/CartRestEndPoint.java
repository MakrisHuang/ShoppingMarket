package com.makris.site.endpoint;

import com.makris.config.annotation.RestEndpoint;
import com.makris.site.entities.Cart;
import com.makris.site.entities.ShoppingItem;
import com.makris.site.entities.UserPrincipal;
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
public class CartRestEndPoint {

    @Inject
    CartService cartService;

    private static final Logger logger = LogManager.getLogger();

    @RequestMapping(value = "cart/add", method = RequestMethod.POST)
    @ResponseBody
    public Cart addItemToCart(@RequestBody ShoppingItem shoppingItem,
                             HttpServletRequest request){
        HttpSession session = request.getSession();
        UserPrincipal customer = (UserPrincipal)session.getAttribute(UserPrincipal.SESSION_ATTRIBUTE_KEY);

        logger.info(shoppingItem);
        logger.info(shoppingItem.getId());

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



    private Cart updateItemInCart(Cart cart,
                                  ShoppingItem shoppingItem,
                                  Integer newAmount){
        return this.cartService.updateItemAmount(cart, shoppingItem, newAmount);
    }

    private Cart removeItemInCart(Cart cart, ShoppingItem shoppingItem){
        return this.cartService.removeItem(cart, shoppingItem);
    }
}
