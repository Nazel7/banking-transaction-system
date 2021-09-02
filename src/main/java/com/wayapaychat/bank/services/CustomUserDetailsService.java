package com.wayapaychat.bank.services;

import com.wayapaychat.bank.entity.models.SecureUserModel;
import com.wayapaychat.bank.repository.SecureUserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetails, UserDetailsService {

    private static final long serialVersionUID = 2343412201042424247L;
    protected SecureUserModel user;

    @Autowired
    private SecureUserRepo repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        user = repository.getSecureUserByUsername(username);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getAuthority());

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                                                                      user.getPassword(),
                                                                      Arrays.asList(authority));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getAuthority());
        try {
            return Arrays.asList(authority);
        } catch (Exception exception) {
            return null;
        }

    }

    public String getActiveRole() {

        return user.getAuthority();
    }


    @Override
    public String getPassword() {
        try {
            return user.getPassword();
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public String getUsername() {
        try {
            return user.getUsername();
        } catch (Exception exception) {
            return null;
        }

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
