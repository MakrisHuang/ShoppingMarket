package com.makris.site.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.servlet.http.HttpSession;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.security.Principal;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UserPrincipal_Username", columnNames = "Username")
}, name = "UserPrincipal")
@XmlAccessorType(XmlAccessType.NONE)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class UserPrincipal implements Principal, Cloneable, Serializable
{
    private static final long serialVersionUID = 1L;

    public static final String SESSION_ATTRIBUTE_KEY = "com.makris.user.principal";

    private long id;

    private String username;

    private byte[] password;

    private String telPhone;

    private String postCode;

    private String address;

    private String email;

    @Id
    @Column(name = "UserId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement
    @JsonProperty
    public long getId()
    {
        return this.id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    @Override
    @Transient
    public String getName()
    {
        return this.username;
    }

    @Basic
    @XmlElement
    @JsonProperty
    public String getUsername()
    {
        return this.username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @Basic
    @Column(name = "HashedPassword")
    public byte[] getPassword()
    {
        return this.password;
    }

    public void setPassword(byte[] password)
    {
        this.password = password;
    }

    @Basic
    @XmlElement
    @JsonProperty
    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    @Basic
    @XmlElement
    @JsonProperty
    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Basic
    @XmlElement
    @JsonProperty
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @XmlElement
    @JsonProperty
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode()
    {
        return this.username.hashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        return other instanceof UserPrincipal &&
                ((UserPrincipal)other).username.equals(this.username);
    }

    @Override
    @SuppressWarnings("CloneDoesntDeclareCloneNotSupportedException")
    protected UserPrincipal clone()
    {
        try {
            return (UserPrincipal)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); // not possible
        }
    }

    @Override
    public String toString()
    {
        return this.username;
    }

    public static Principal getPrincipal(HttpSession session)
    {
        return session == null ? null :
                (Principal)session.getAttribute(SESSION_ATTRIBUTE_KEY);
    }

    public static void setPrincipal(HttpSession session, Principal principal)
    {
        session.setAttribute(SESSION_ATTRIBUTE_KEY, principal);
    }

    public static void removePrincipal(HttpSession session, Principal principal){
        session.removeAttribute(SESSION_ATTRIBUTE_KEY);
    }
}