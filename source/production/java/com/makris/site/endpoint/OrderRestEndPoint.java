package com.makris.site.endpoint;

import com.makris.config.annotation.RestEndpoint;
import com.makris.exception.ResourceNotFoundException;
import com.makris.site.entities.Order;
import com.makris.site.entities.OrderWebServiceList;
import com.makris.site.entities.ShoppingItem;
import com.makris.site.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.inject.Inject;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

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
        logger.error("order/{id}");
        if (this.orderService.getOrder(id) == null){
            throw new ResourceNotFoundException();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "OPTIONS,HEAD,GET,POST");
        return new ResponseEntity<>(null, headers, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "order/{customer}", method = RequestMethod.GET)
    @ResponseBody @ResponseStatus(HttpStatus.OK)
    public OrderWebServiceList getOrders(@PathVariable("customer") String customer){
        OrderWebServiceList list = new OrderWebServiceList();
        list.setOrders(this.orderService.getAllOrdersByCustomer(customer));

        return list;
    }

    @RequestMapping(value = "order", method = RequestMethod.POST)
    public ResponseEntity<Order> create(@RequestBody OrderForm form){
        Order order = new Order();
        order.setCustomer(null);
        order.setItems(form.getShoppingItems());
        order.setPrice(form.getPrice());
        order.setStatus(form.getStatus());

        this.orderService.save(order);

        String uri = ServletUriComponentsBuilder.
                fromCurrentServletMapping().path("/order/{id}").buildAndExpand(order.getId()).toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uri);
        return new ResponseEntity<>(order, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "order/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") long id){
        if (this.orderService.getOrder(id) == null){
            throw new ResourceNotFoundException();
        }
        this.orderService.deleteOrder(id);
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

        @XmlElement(name = "shoppingItem")
        public List<ShoppingItem> getShoppingItems() {
            return shoppingItems;
        }

        public void setShoppingItems(List<ShoppingItem> shoppingItems) {
            this.shoppingItems = shoppingItems;
        }
    }

}