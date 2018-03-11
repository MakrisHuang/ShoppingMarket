package com.makris.site.security;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.security.auth.Subject;
import java.io.Serializable;
import java.security.Principal;
import java.util.List;

@Entity
public class JwtUserPrincipal implements Principal, Cloneable,  Serializable{
    private Long userId;
    private String password;
    private String username;
    private List<String> roles;
    private boolean isNonExpired;
    private boolean isAccountEnabled;

    public JwtUserPrincipal(){

    }

    public JwtUserPrincipal(Long userId, String username, boolean isNonExpired,
                            List<String> roles, boolean isAccountEnabled) {
        this.userId = userId;
        this.username = username;
        this.roles = roles;
        this.isNonExpired = isNonExpired;
        this.isAccountEnabled = isAccountEnabled;
    }

    @Id
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    @Transient
    public String getName() {
        return null;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }

    public boolean isNonExpired() {
        return isNonExpired;
    }

    public void setNonExpired(boolean nonExpired) {
        isNonExpired = nonExpired;
    }

    public boolean isAccountEnabled() {
        return isAccountEnabled;
    }

    public void setAccountEnabled(boolean accountEnabled) {
        isAccountEnabled = accountEnabled;
    }

    @Transient
    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
