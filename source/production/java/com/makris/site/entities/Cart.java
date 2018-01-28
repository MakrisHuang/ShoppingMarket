package com.makris.site.entities;

import javax.xml.bind.annotation.XmlTransient;
import java.util.List;
import java.util.Map;

public class Cart {
    public static final String CART_IDENTIFIER = "cart";

    private List<Map<String, Object>> cartItems;
    private Integer total;

    public List<Map<String, Object>> getCartItems() {
        return cartItems;
    }

    @XmlTransient
    public void setCartItems(List<Map<String, Object>> cartItems) {
        this.cartItems = cartItems;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    /*
            Helper function
         */
    public void calculateTotalPrice(){
        this.total = 0;
        for (Map<String, Object> itemInfo: this.cartItems) {
            Integer priceOfItems = 0;
            ShoppingItem shoppingItem = (ShoppingItem) itemInfo.get("item");
            Integer numOfItems = (Integer) itemInfo.get("count");
            priceOfItems += (shoppingItem.getPrice() * numOfItems);
            this.total += priceOfItems;
        }
    }
}
