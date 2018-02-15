package com.makris.site.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Cart")
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Cart implements Serializable{

    private long id;

    private UserPrincipal customer;

    private List<CartItem> cartItems = new ArrayList<>();

    @Id
    @Column(name = "CartId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement
    @JsonProperty
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserId")
    @XmlElement
    @JsonProperty
    public UserPrincipal getCustomer() {
        return customer;
    }

    public void setCustomer(UserPrincipal customer) {
        this.customer = customer;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL,
        orphanRemoval = true)
    @JoinTable(name = "Cart_CartItem",
                joinColumns = {@JoinColumn(name = "CartId")},
                inverseJoinColumns = {@JoinColumn(name = "CartItemId")})
    @OrderColumn(name = "SortKey")
    @XmlElement
    @JsonProperty
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    /*
        Helper function
     */
    public void addCartItem(CartItem cartItem){
        this.cartItems.add(cartItem);
    }

    public void updateCartItemAmount(CartItem cartItem, Integer amount){
        for (CartItem item:this.cartItems) {
            if (item.getShoppingItem().getId() == cartItem.getShoppingItem().getId()){
                // find identical one, update the amount
                item.setAmount(amount);
                this.cartItems.set(this.cartItems.indexOf(item), item);
            }
        }
    }

    public void removeCartItem(CartItem cartItem){
        for (CartItem item:this.cartItems) {
            if (item.getShoppingItem().getId() == cartItem.getShoppingItem().getId()){
                // find identical one, remove it
                this.cartItems.remove(item);
            }
        }
    }
}
