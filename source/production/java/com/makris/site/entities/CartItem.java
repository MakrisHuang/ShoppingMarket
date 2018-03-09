package com.makris.site.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.LogManager;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Map;

@Entity
@Table(name = "CartItem")
@XmlAccessorType(XmlAccessType.NONE)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class CartItem {
    private long id;

    private Integer amount;

    private ShoppingItem shoppingItem;

    @Id
    @Column(name = "CartItemId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement
    @JsonProperty
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @XmlElement
    @JsonProperty
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ShoppingItemId")
    @JsonProperty("shoppingItem")
    @XmlElement
    public ShoppingItem getShoppingItem() {
        return shoppingItem;
    }

    public void setShoppingItem(ShoppingItem shoppingItem) {
        this.shoppingItem = shoppingItem;
    }

    @JsonProperty("shoppingItemRest")
    public void unpackShoppingItem(Map<String, Object> shoppingItemMap){
        ShoppingItem shoppingItem = new ShoppingItem();
        Number id = (Integer)shoppingItemMap.get("id");
        Long idLong = id.longValue();
        shoppingItem.setId(idLong);
        LogManager.getLogger().info(shoppingItemMap);
        shoppingItem.setCategory((String)shoppingItemMap.get("category"));
        shoppingItem.setDescription((String)shoppingItemMap.get("description"));
        shoppingItem.setName((String)shoppingItemMap.get("name"));
        shoppingItem.setPrice((Integer)shoppingItemMap.get("price"));
        shoppingItem.setImage(((String)shoppingItemMap.get("image")).getBytes());
        this.shoppingItem = shoppingItem;
    }

    @Override
    public String toString(){
        return  "id: " + this.id + ", amount: " + this.amount + ", shoppingItem: {"
                + this.shoppingItem.toString() + "}";
    }
}
