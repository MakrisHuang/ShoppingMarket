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
        entities = {@EntityResult(entityClass = Order.class)},
        columns = {@ColumnResult(name = "_ft_scoreColumn", type = Double.class)}
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
    private List<ShoppingItem> items = new ArrayList<>();

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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinTable(name = "Orders_ShoppingItem",
            // JoinColumn name為SQL表實體的名稱，referencedColName為@Id所代表的column name
            joinColumns = { @JoinColumn(name = "OrderId")},
            inverseJoinColumns = {@JoinColumn(name = "ShoppingItemId")})
    @OrderColumn(name = "SortKey")
    @XmlElement(name = "shoppingItem")
    @JsonProperty
    public List<ShoppingItem> getItems() {
        return items;
    }

    public void setItems(List<ShoppingItem> items) {
        this.items = items;
    }

    @Transient
    public int getNumberOfShoppingItems(){
        return this.items.size();
    }
}
