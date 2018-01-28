package com.makris.site.endpoint;

import com.makris.config.annotation.RestEndpoint;
import com.makris.exception.ResourceNotFoundException;
import com.makris.site.entities.*;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;
import java.util.Map;

@RestEndpoint
public class OrderRestEndPoint {
    private static final Logger logger = LogManager.getLogger();

    @Inject
    OrderService orderService;

    @RequestMapping(value = "order", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> discover(){
        logger.debug("order in options");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "OPTIONS,HEAD,GET,POST");
        return new ResponseEntity<>(null, headers, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "order/{id}", method = RequestMethod.OPTIONS)
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
        顯示所有訂單
     */
    @RequestMapping(value = "order", method = RequestMethod.GET)
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
    @RequestMapping(value = "order/cart", method = RequestMethod.GET)
    public Cart showCart(Map<String, Object> model, HttpServletRequest request){
        HttpSession session = request.getSession();
        if (session.getAttribute(Cart.CART_IDENTIFIER) == null){
            return new Cart();
        }
        Cart cart = (Cart)session.getAttribute(Cart.CART_IDENTIFIER);
        cart.calculateTotalPrice();
        return cart;
    }


    /*
        送出訂單，若成功，回傳訂單內容
     */
    @RequestMapping(value = "order/new", method = RequestMethod.POST)
    public ResponseEntity<Order> create(@RequestBody OrderForm form,
                                        HttpServletRequest request){
        Order order = new Order();

        HttpSession session = request.getSession();
        UserPrincipal customer = (UserPrincipal)session.getAttribute(UserPrincipal.SESSION_ATTRIBUTE_KEY);
        if (customer != null) {
            order.setCustomer(customer); // get user info from session
            order.setItems(form.getShoppingItems());
            order.setPrice(form.getPrice());
            order.setStatus(form.getStatus());

            this.orderService.save(order);

            String uri = ServletUriComponentsBuilder.
                    fromCurrentServletMapping().path("/order/{id}").buildAndExpand(order.getId()).toString();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", uri);
            return new ResponseEntity<>(order, headers, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(new Order(), null, HttpStatus.NOT_MODIFIED);
        }
    }

    @XmlRootElement(name = "order")
    public static class OrderForm{
        private Integer price;
        private String status;
        private String customer;
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

        @XmlTransient
        public List<ShoppingItem> getShoppingItems() {
            return shoppingItems;
        }

        public void setShoppingItems(List<ShoppingItem> shoppingItems) {
            this.shoppingItems = shoppingItems;
        }
    }

}