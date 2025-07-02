package com.corp.match.security;

import com.corp.match.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final UserAccount userAccount;

    public CustomUserDetails(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    @Override
    public String getUsername() {
        return userAccount.getPersonalEmail();
    }

    @Override
    public String getPassword() {
        return userAccount.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Optional: Add roles if needed
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
        return userAccount.isWorkEmailVerified(); // Only allow login if verified
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }
}
