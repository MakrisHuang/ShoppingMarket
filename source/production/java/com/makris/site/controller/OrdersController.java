package com.makris.site.controller;

import com.makris.config.annotation.WebController;
import com.makris.site.entities.ShoppingItem;
import com.makris.site.entities.UserPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

@WebController
public class OrdersController {
    private static final String JSP_ORDERS_VIEWALL = "/profile/orders_viewall";
    private static final String JSP_ORDERS_CREATE = "/profile/orders_create";
    private static final String JSP_ORDERS_FINISH = "/profile/orders_finish";
    private static final String JSP_ORDERS_SINGLE_ORDER = "/profile/orders_single_order";

    @RequestMapping(value = "orders", method = RequestMethod.GET)
    public ModelAndView orders(@RequestParam("action") String action,
            Map<String, Object> model,
                               HttpServletRequest request){
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

        switch (action){
            case "viewall":
                return new ModelAndView(JSP_ORDERS_VIEWALL);
            case "create":
                return new ModelAndView(JSP_ORDERS_CREATE);
            case "finish":
                return new ModelAndView(JSP_ORDERS_FINISH);
            default:
                return new ModelAndView(JSP_ORDERS_VIEWALL);
        }
    }

    @RequestMapping(value = "orders/{id}", method = RequestMethod.GET)
    public ModelAndView getAnOrder(@PathVariable("id") Integer orderId,
                                   Map<String, Object> model,
                                   HttpServletRequest request){
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

        // put requested order id again
        model.put("orderId", orderId);

        return new ModelAndView(JSP_ORDERS_SINGLE_ORDER);
    }

    // Unused
    @XmlRootElement(name = "order")
    public static class OrderForm{
        private Integer price;
        private String status;
        private String customer;
        private String email;
        private List<ShoppingItem> shoppingItems;

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCustomer() {
            return customer;
        }

        public void setCustomer(String customer) {
            this.customer = customer;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @XmlElement(name = "shoppingItems")
        public List<ShoppingItem> getShoppingItems() {
            return shoppingItems;
        }

        public void setShoppingItems(List<ShoppingItem> shoppingItems) {
            this.shoppingItems = shoppingItems;
        }
    }
}
