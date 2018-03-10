package com.makris.site.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class OrderWebServiceList {
    private List<Order> orders;

    @XmlElement(name = "order")
    @JsonProperty
    public List<Order> getOrders(){
        return this.orders;
    }

    public void setOrders(List<Order> orders){
        this.orders = orders;
    }
}
