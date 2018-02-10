package com.makris.site.controller;

import com.makris.config.annotation.WebController;
import com.makris.site.entities.UserPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@WebController
public class OrdersController {
    private static final String JSP_ORDERS = "/profile/orders";

    @RequestMapping(value = "orders", method = RequestMethod.GET)
    public ModelAndView orders(Map<String, Object> model, HttpServletRequest request){
        HttpSession session = request.getSession();

        UserPrincipal customer = (UserPrincipal) session.getAttribute(UserPrincipal.SESSION_ATTRIBUTE_KEY);
        if (customer == null){
            model.put("loginFailed", true);
        }else{
            model.put("userName", customer.getName());
            model.put("loginFailed", false);
        }
        model.put("loginForm", new HomeController.LoginForm());
        model.put("registerForm", new HomeController.RegisterForm());
        // need to send session?
        return new ModelAndView(JSP_ORDERS);
    }
}
