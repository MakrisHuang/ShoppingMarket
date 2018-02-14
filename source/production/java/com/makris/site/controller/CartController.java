package com.makris.site.controller;

import com.makris.config.annotation.WebController;
import com.makris.site.entities.Cart;
import com.makris.site.entities.ShoppingItem;
import com.makris.site.entities.UserPrincipal;
import com.makris.site.service.CartService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.Map;

@WebController
public class CartController {

    @Inject
    CartService cartService;

    private static final Logger logger = LogManager.getLogger();

    private static final String JSP_CART = "/profile/cart";

    @RequestMapping(value = "cart")
    public ModelAndView viewCart(Map<String, Object> model,
                                 HttpServletRequest request){
        return new ModelAndView(JSP_CART);
    }

    @RequestMapping(value = "cart", method = RequestMethod.POST)
    @ResponseBody
    public Cart cartHandling(@ModelAttribute @NotNull ShoppingItem shoppingItem,
                             @RequestParam Map<String, Object> param,
                             HttpServletRequest request){
        HttpSession session = request.getSession();
        UserPrincipal customer = (UserPrincipal)session.getAttribute(UserPrincipal.SESSION_ATTRIBUTE_KEY);

        String action = (String)param.get("action");
        logger.debug(param);
        Cart cart = this.cartService.findCartByCustomer(customer);
        if (cart == null) cart = new Cart();

        switch (action){
            case "add":
                cart = this.addItemToCart(cart, shoppingItem);
                break;
            case "update":
                Integer newAmount = (Integer)param.get("newAmount");
                cart = this.updateItemInCart(cart, shoppingItem, newAmount);
                break;
            case "remove":
                cart = this.removeItemInCart(cart, shoppingItem);
                break;
            default:
                logger.error("Unknown action {}", action);
                break;
        }
        return cart;
    }

    private Cart addItemToCart(Cart cart, ShoppingItem shoppingItem){
        return this.cartService.addItemToCart(cart, shoppingItem);
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
