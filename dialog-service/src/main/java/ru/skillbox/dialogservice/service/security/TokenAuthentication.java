package ru.skillbox.dialogservice.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class TokenAuthentication implements Authentication {

    private final List<GrantedAuthority> authorities;
    private final boolean authenticated;
    private final String account;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Principal getPrincipal() {
        return () -> account;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        //Field is final, method need only to override a parent
    }

    @Override
    public String getName() {
        return account;
    }
}
