package com.makris.site.endpoint;

import com.makris.config.annotation.RestEndpoint;
import com.makris.exception.ResourceNotFoundException;
import com.makris.site.entities.*;
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
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@RestEndpoint
@ResponseBody
public class OrdersRestEndPoint {
    private static final Logger logger = LogManager.getLogger();

    private Set<Order> ordersInQueue = new HashSet<>();

    @Inject
    OrderService orderService;

    @Inject
    CartService cartService;

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
    public Order showOrder(@PathVariable("id") long id){
        Order order = this.orderService.getOrder(id);
        if (order == null){
            throw new ResourceNotFoundException();
        }
        return order;
    }

    // 顯示所有訂單
    @RequestMapping(value = "orders/viewall", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public OrderWebServiceList getOrders(HttpServletRequest request){
        HttpSession session = request.getSession();
        UserPrincipal customer = (UserPrincipal)session.getAttribute(UserPrincipal.SESSION_ATTRIBUTE_KEY);

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
    public ResponseEntity<Void> create(@RequestBody Cart cart,
                                        HttpServletRequest request){
        HttpSession session = request.getSession();
        UserPrincipal customer = (UserPrincipal)session.getAttribute(UserPrincipal.SESSION_ATTRIBUTE_KEY);

        logger.info("[orders/create] cart: ");
        logger.info(cart.toString());

        if (customer != null){
            // create order for the customer
            Order order = new Order();
            order.setId(1); // 該id需唯一
            order.setCustomer(customer);
            order.setStatus("created");
            order.setDateCreated(Instant.now());
            order.setPrice(calculateTotalPrice(cart));
            order.setItems(cart.getCartItems());

            this.ordersInQueue.add(order);
        }
        return null;
    }

    // 向伺服器索取暫時的Order
    @RequestMapping(value = "orders/temporaryOrder", method = RequestMethod.GET)
    public Order getTemporaryOrder(HttpServletRequest request){
        HttpSession session = request.getSession();
        UserPrincipal customer = (UserPrincipal)session.getAttribute(UserPrincipal.SESSION_ATTRIBUTE_KEY);

        // find temporary order by customer
        Order tempOrder = null;
        for (Order order: this.ordersInQueue){
            if (order.getCustomer().getId() == customer.getId()){
                tempOrder = order;
            }
        }
        return tempOrder;
    }

    @RequestMapping(value = "orders/finish", method = RequestMethod.POST)
    public Integer finishOrder(@RequestBody Order order,
                             HttpServletRequest request){
        HttpSession session = request.getSession();
        UserPrincipal customer = (UserPrincipal)session.getAttribute(UserPrincipal.SESSION_ATTRIBUTE_KEY);

        logger.info(order.getItems());

        if (customer != null){
            // save order to db
            this.orderService.save(order);

            // clear cart
//            this.cartService.deleteCartByCustomer(customer);

            // clear temporary order
            for (Order tempOrder: this.ordersInQueue){
                if (tempOrder.getCustomer().getId() == customer.getId()){
                    this.ordersInQueue.remove(tempOrder);
                }
            }
            // send email to customer

            // retrieve saved order from database
            Order savedOrder = this.orderService.getLatestOrder(customer);
            Long id = savedOrder.getId();
            return id.intValue();
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