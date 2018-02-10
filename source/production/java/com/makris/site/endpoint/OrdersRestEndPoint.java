package com.makris.site.endpoint;

import com.makris.config.annotation.RestEndpoint;
import com.makris.exception.ResourceNotFoundException;
import com.makris.site.entities.Order;
import com.makris.site.entities.OrderWebServiceList;
import com.makris.site.entities.ShoppingItem;
import com.makris.site.entities.UserPrincipal;
import com.makris.site.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.Instant;
import java.util.List;

@RestEndpoint
public class OrdersRestEndPoint {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    OrderService orderService;

    @RequestMapping(value = "orders", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> discover(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "OPTIONS,HEAD,GET,POST");
        return new ResponseEntity<>(null, headers, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "orders/{id}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> discover(@PathVariable("id") long id){
        // need to check user session
        if (this.orderService.getOrder(id) == null){
            throw new ResourceNotFoundException();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "OPTIONS,HEAD,GET,POST");
        return new ResponseEntity<>(null, headers, HttpStatus.NO_CONTENT);
    }

    /*
        顯示單一筆訂單
     */
    @RequestMapping(value = "orders/{id}", method = RequestMethod.GET)
    public Order showOrder(@PathVariable("id") long id){
        Order order = this.orderService.getOrder(id);
        if (order == null){
            throw new ResourceNotFoundException();
        }
        return order;
    }

    /*
        顯示所有訂單
     */
    @RequestMapping(value = "orders", method = RequestMethod.GET)
    @ResponseBody @ResponseStatus(HttpStatus.OK)
    public OrderWebServiceList getOrders(HttpServletRequest request){
        HttpSession session = request.getSession();
        UserPrincipal customer = (UserPrincipal)session.getAttribute(UserPrincipal.SESSION_ATTRIBUTE_KEY);

        OrderWebServiceList list = new OrderWebServiceList();
        list.setOrders(this.orderService.getAllOrdersByCustomer(customer));

        return list;
    }

    /*
        從Session獲取購物車內的商品
    */
//    @RequestMapping(value = "orders/cart", method = RequestMethod.GET)
//    public Cart showCart(Map<String, Object> model, HttpServletRequest request){
//        HttpSession session = request.getSession();
//        if (session.getAttribute(Cart.CART_IDENTIFIER) == null){
//            return new Cart();
//        }
//        Cart cart = (Cart)session.getAttribute(Cart.CART_IDENTIFIER);
//        cart.calculateTotalPrice();
//        return cart;
//    }


    /*
        送出訂單，若成功，回傳訂單內容
     */
    @RequestMapping(value = "orders/new", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Order> create(@RequestBody OrderForm form,
                                        HttpServletRequest request){
        Order order = new Order();

        HttpSession session = request.getSession();

        // 從session pool中獲取customer的password
        UserPrincipal customer = (UserPrincipal)session.getAttribute(UserPrincipal.SESSION_ATTRIBUTE_KEY);
        try{
            String customerUsername = customer.getUsername();
            String customerEmail = customer.getEmail();

            // 從request body獲取client端的username和password
            String clientUsername = form.getCustomer();
            String clientEmail = form.getEmail();

            if (customerUsername == clientUsername && customerEmail == clientEmail) {
                // 身份驗證通過，可建立訂單
                order.setCustomer(customer); // get user info from session
                order.setItems(form.getShoppingItems());
                order.setPrice(form.getPrice());
                order.setStatus(form.getStatus());
                order.setDateCreated(Instant.now());

                this.orderService.save(order);

                String uri = ServletUriComponentsBuilder.
                        fromCurrentServletMapping().path("/order/{id}").buildAndExpand(order.getId()).toString();
                HttpHeaders headers = new HttpHeaders();
                headers.add("Location", uri);
                return new ResponseEntity<>(order, headers, HttpStatus.CREATED);
            }else{
                // 身份驗證失敗，需回傳失敗訊息
                return new ResponseEntity<>(order, null, HttpStatus.OK);
            }
        } catch (NullPointerException e){
            return new ResponseEntity<>(null, null, HttpStatus.NOT_MODIFIED);
        }
    }

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