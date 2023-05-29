package com.ctg.pipeline.execute.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableList;
//import static java.util.stream.Collectors.toList;

/**
 * @author zhiHuang
 * @date 2022/11/16 18:06
 **/
public class User implements Serializable {

    public static final long serialVersionUID = 7392392099262597885L;

    protected String email;
    protected String username;
    protected String firstName;
    protected String lastName;

    protected Collection<String> roles = new ArrayList<>();
    protected Collection<String> allowedAccounts = new ArrayList<>();

    /*@Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .filter(StringUtils::hasText)
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }*/

    /** Not used */
    @JsonIgnore
//    @Override
    public String getPassword() {
        return "";
    }

//    @Override
    public String getUsername() {
        return this.username == null ? this.email : this.username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Collection<String> getRoles() {
        return unmodifiableCollection(roles);
    }

    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }

    public Collection<String> getAllowedAccounts() {
        return allowedAccounts;
    }

    public void setAllowedAccounts(Collection<String> allowedAccounts) {
        this.allowedAccounts = allowedAccounts;
    }

    public User asImmutable() {
        return new ImmutableUser();
    }

   /* @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }*/

    private final class ImmutableUser extends User {
        ImmutableUser() {
            this.email = User.this.email;
            this.username = User.this.username;
            this.firstName = User.this.firstName;
            this.lastName = User.this.lastName;
            this.roles = unmodifiableList(new ArrayList<>(User.this.roles));
            this.allowedAccounts = unmodifiableList(new ArrayList<>(User.this.allowedAccounts));
        }

        @Override
        public void setEmail(String email) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setUsername(String username) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setFirstName(String firstName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setLastName(String lastName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setRoles(Collection<String> roles) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setAllowedAccounts(Collection<String> allowedAccounts) {
            throw new UnsupportedOperationException();
        }

        @Override
        public User asImmutable() {
            return this;
        }
    }
}