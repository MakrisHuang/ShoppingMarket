package com.makris.site.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class ShoppingWebServiceList {
    private List<ShoppingItem> shoppingItems;

    @XmlElement(name = "shoppingItem")
    public List<ShoppingItem> getShoppingItems(){
        return this.shoppingItems;
    }

    public void setShoppingItems(List<ShoppingItem> shoppingItems) {
        this.shoppingItems = shoppingItems;
    }
}
