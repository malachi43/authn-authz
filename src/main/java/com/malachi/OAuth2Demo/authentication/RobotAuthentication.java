package com.malachi.OAuth2Demo.authentication;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RobotAuthentication implements Authentication {
    private final static String PREFIX = "ROLE_";
    private final List<GrantedAuthority> authorities;
    private final String password;
    private final boolean isAuthenticated;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(PREFIX + "robot"));
    }

    private RobotAuthentication(List<GrantedAuthority> authorities, String password){
      this.authorities = authorities;
      this.password = password;
      this.isAuthenticated = this.password == null;
    }


    public static Authentication unauthenticated(String password){
        return new RobotAuthentication(Collections.emptyList(),password);
    }

    public static Authentication authenticated(){
        return new RobotAuthentication(AuthorityUtils.createAuthorityList(PREFIX +"robot"),null);
    }

    public String getPassword(){
        return this.password;
    }

    @Override
    public @Nullable Object getCredentials() {
        return null;
    }

    @Override
    public @Nullable Object getDetails() {
        return null;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return getName();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("This should be immutable.");
    }

    @Override
    public Builder<?> toBuilder() {
        return Authentication.super.toBuilder();
    }

    @Override
    public String getName() {
        return "Malachi";
    }
}
