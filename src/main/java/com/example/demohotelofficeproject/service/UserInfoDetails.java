package com.example.demohotelofficeproject.service;

import com.example.demohotelofficeproject.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserInfoDetails implements UserDetails {
    private String userEmail;
    private String userPassword;
    List<GrantedAuthority> authorities = new ArrayList<>();

    public UserInfoDetails(User user) {
        userEmail = user.getEmail();
        userPassword = user.getPassword();
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());
        authorities.add(authority);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userEmail;
    }

    @Override
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
    }
}
