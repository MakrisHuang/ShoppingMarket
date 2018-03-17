package com.makris.site.endpoint;

import com.makris.config.annotation.RestEndpoint;
import com.makris.exception.ResourceNotFoundException;
import com.makris.site.entities.*;
import com.makris.site.security.JwtUtils;
import com.makris.site.service.CartService;
import com.makris.site.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@RestEndpoint
@ResponseBody
public class OrdersRestEndPoint {
    private static final Logger logger = LogManager.getLogger();

    @Inject OrderService orderService;
    @Inject CartService cartService;
    @Inject JwtUtils jwtUtils;

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


    // 顯示單一筆訂單
    @RequestMapping(value = "orders/{id}", method = RequestMethod.POST)
    public Order showOrder(@PathVariable("id") long id, HttpServletRequest request){
        UserPrincipal customer = jwtUtils.getUserFromHttpRequest(request, false);
        if (customer != null) {
            Order order = this.orderService.getOrder(id);
            if (order == null){
                throw new ResourceNotFoundException();
            }
            return order;
        }
        return null;
    }

    // 顯示所有訂單
    @RequestMapping(value = "orders/viewall", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public OrderWebServiceList getOrders(HttpServletRequest request){
        UserPrincipal customer = jwtUtils.getUserFromHttpRequest(request, false);
        OrderWebServiceList list = null;
        if (customer != null) {
            list = new OrderWebServiceList();
            list.setOrders(this.orderService.getAllOrdersByCustomer(customer));
            logger.info(list.getOrders().get(0).toString());
        }

        return list;
    }

    // 傳送暫時的order到伺服器
    @RequestMapping(value = "orders/create", method = RequestMethod.POST)
    public ResponseEntity<Order> create(@RequestBody Cart cart,
                                        HttpServletRequest request){
        logger.info("[orders/create] cart: ");
        logger.info(cart.toString());

        UserPrincipal customer = jwtUtils.getUserFromHttpRequest(request, false);
        if (customer != null){
            // create order for the customer
            Order order = new Order();
            order.setId(1); // 該id需唯一
            order.setCustomer(customer);
            order.setStatus("created");
            order.setDateCreated(Instant.now());
            order.setPrice(calculateTotalPrice(cart));

            // create CartItemForOrder
            order.convertCartItemsToCartItemsForOrder(cart.getCartItems());

            return new ResponseEntity<>(order, null, HttpStatus.OK);
        }
        return null;
    }

    @RequestMapping(value = "orders/finish", method = RequestMethod.POST)
    public ResponseEntity<Order> finishOrder(@RequestBody Order order,
                             HttpServletRequest request){
        logger.info(order.getItems());
        UserPrincipal customer = jwtUtils.getUserFromHttpRequest(request, false);
        if (customer != null){
            // save order to db
            this.orderService.save(order);

            // clear cart
            this.cartService.deleteCartByCustomer(customer);

            // send email to customer

            // retrieve saved order from database
            Order savedOrder = this.orderService.getLatestOrder(customer);
            return new ResponseEntity<>(savedOrder, null, HttpStatus.CREATED);
        }
        return null;
    }

    /*
        Helper function
     */
    private Integer calculateTotalPrice(Cart cart){
        Integer total = 0;
        for (CartItem item:cart.getCartItems()) {
            Integer count = item.getAmount() * item.getShoppingItem().getPrice();
            total += count;
        }
        return total;
    }
}