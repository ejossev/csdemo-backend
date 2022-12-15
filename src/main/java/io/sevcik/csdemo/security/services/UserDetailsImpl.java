package io.sevcik.csdemo.security.services;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.sevcik.csdemo.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
    final static String ROLE_PREFIX = "ROLE_";
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    @JsonIgnore
    private String passhash;

    boolean isAdmin;

    public UserDetailsImpl(Long id, String username, String passhash,
                           boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.passhash = passhash;
        this.isAdmin = isAdmin;
    }

    public static UserDetailsImpl build(User user) {

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.isAdmin());
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return passhash;
    }

    @Override
    public String getUsername() {
        return username;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (isAdmin) {
            GrantedAuthority role = new SimpleGrantedAuthority(ROLE_PREFIX + "ADMIN");
            return Collections.singletonList(role);
        }
        return Collections.emptyList();
    }
}