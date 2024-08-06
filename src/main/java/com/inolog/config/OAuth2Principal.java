package com.inolog.config;

import com.inolog.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class OAuth2Principal implements OAuth2User, UserDetails {

    private User user;
    private UserPrincipal principal;
    private Map<String, Object> attributes;
    private String name;

    public OAuth2Principal(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.name = (String) attributes.get("name");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return (String) attributes.get("login");
    }
}
