package com.sankore.bank.services;

import com.sankore.bank.Tables;
import com.sankore.bank.tables.records.SecureUserRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomUserDetailsService implements UserDetails, UserDetailsService {

    private static final long serialVersionUID = 2343412201042424247L;
    protected SecureUserRecord userRecord;
    private final DSLContext dslContext;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        userRecord =
                dslContext.fetchOne(Tables.SECURE_USER, Tables.SECURE_USER.USERNAME.eq(username));
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRecord.getAuthority());

        return new org.springframework.security.core.userdetails.User(userRecord.getUsername(),
                userRecord.getPassword(),
                Arrays.asList(authority));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRecord.getAuthority());
        try {
            return Arrays.asList(authority);
        } catch (Exception exception) {
            return null;
        }

    }

    public String getActiveRole() {

        return userRecord.getAuthority();
    }


    @Override
    public String getPassword() {
        try {
            return userRecord.getPassword();
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public String getUsername() {
        try {
            return userRecord.getUsername();
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
