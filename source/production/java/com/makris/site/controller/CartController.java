package com.makris.site.controller;

import com.makris.config.annotation.WebController;
import com.makris.site.entities.Cart;
import com.makris.site.entities.UserPrincipal;
import com.makris.site.service.CartService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.Map;

@WebController
public class CartController {
    @Inject
    private CartService cartService;

    private static final Logger logger = LogManager.getLogger();

    private static final String ATTR_FORM_LOGIN     = "loginForm";
    private static final String ATTR_FORM_REGISTER  = "registerForm";
    private static final String ATTR_USERNAME       = "userName";

    private final String JSP_CART = "/profile/cart";

    @RequestMapping(value = "cart", method = RequestMethod.GET)
    public ModelAndView viewCart(Map<String, Object> model, HttpSession session){
        UserPrincipal customer = (UserPrincipal) UserPrincipal.getPrincipal(session);
        if (customer != null){
            model.put("loginFailed", false);
            model.put(ATTR_USERNAME, customer.getUsername());

            // find cart from database
            Cart cart = this.cartService.findCartByCustomer(customer);
            model.put("cart", cart);
        }
        else {
            model.put("loginFailed", true);
        }
        model.put(ATTR_FORM_LOGIN, new HomeController.LoginForm());
        model.put(ATTR_FORM_REGISTER, new HomeController.RegisterForm());
        return new ModelAndView(JSP_CART);
    }
}
