package com.makris.site.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.makris.site.converters.InstantConverter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Orders")
@SqlResultSetMapping(
    name = "searchResultMapping.order",
    entities = {
        @EntityResult(
            entityClass = Order.class,
            fields = {
                @FieldResult(name = "id", column = "OrderId"),
                @FieldResult(name = "userId", column = "UserId")
            }
        )
    }
)
@XmlRootElement(namespace = "http://example.com/xmlns/shopping", name = "order")
@XmlAccessorType(XmlAccessType.NONE)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Order implements Serializable{

    private static final long serialVersionUID = 1L;

    private long id;

    private Integer price;

    private UserPrincipal customer;

    private String status;

    private Instant dateCreated;

    @Valid
    private List<CartItemForOrder> items = new ArrayList<>();

    @Id
    @Column(name = "OrderId")
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
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    // Many order could be owned by one customer
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "UserId")
    @XmlElement
    @JsonProperty
    public UserPrincipal getCustomer() {
        return customer;
    }

    public void setCustomer(UserPrincipal customer) {
        this.customer = customer;
    }

    @Basic
    @XmlElement
    @JsonProperty
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Convert(converter = InstantConverter.class)
    @XmlElement
    @XmlSchemaType(name = "dateTime")
    @JsonProperty
    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinTable(name = "Orders_CartItemForOrder",
            // JoinColumn name為SQL表實體的名稱，referencedColName為@Id所代表的column name
            joinColumns = { @JoinColumn(name = "OrderId")},
            inverseJoinColumns = {@JoinColumn(name = "CartItemForOrderId")})
    @OrderColumn(name = "SortKey")
    @XmlElement
    @JsonProperty
    public List<CartItemForOrder> getItems() {
        return items;
    }

    public void setItems(List<CartItemForOrder> items) {
        this.items = items;
    }

    @Transient
    public int getNumberOfCartItems(){
        return this.items.size();
    }

    @Override
    public String toString(){
        String itemsConcatStr = "";
        for (CartItemForOrder item: this.items) {
            itemsConcatStr = itemsConcatStr + item.toString() + " ";
        }
        String orderStr = "id: " + this.id
                + ", customer: " + this.customer.toString()
                + ", dateCreate: " + this.dateCreated.toString()
                + ", price: " + this.price.toString()
                + ", status: " + this.status
                + ", items: [" + itemsConcatStr + "]";

        return orderStr;
    }

    public void convertCartItemsToCartItemsForOrder(List<CartItem> cartItemList){
        for (CartItem cartItem: cartItemList){
            CartItemForOrder cartItemForOrder = new CartItemForOrder(cartItem);
            this.items.add(cartItemForOrder);
        }
    }
}
