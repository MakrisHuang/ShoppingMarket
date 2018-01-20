package com.makris.site.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

@Entity
@XmlRootElement(name = "shoppingItem")
@XmlAccessorType(XmlAccessType.NONE)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class ShoppingItem implements Serializable{
    private static final long serialVersionUID = 1L;

    private long id;

    private String name;

    private String category;

    private String description;

    private Double price;

    private byte[] image;

    @Id
    @Column(name = "ShoppingItemId")
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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @XmlElement
    @JsonProperty
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Basic
    @XmlElement
    @JsonProperty
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @XmlElement
    @JsonProperty
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @XmlElement
    @XmlSchemaType(name = "base64Binary")
    @JsonProperty
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
