package com.surrogate.numy.models.login;

/*
 * Esta clase va a proveerle el usuario a Spring Security
 * Modificada por Santiago Garcia 2025 para que acepte ids de usuario
 * */

import com.surrogate.numy.models.bussiness.Usuario;
import com.surrogate.numy.utils.UserDetailsWithId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;


public record UserPrincipal(Usuario usuario) implements UserDetailsWithId {


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(usuario.getRol()));
    }

    @Override
    public String getPassword() {
        return usuario.getPasswordHash();
    }

    @Override
    public long getId() {
        return usuario.getId();
    }

    @Override
    public String getUsername() {
        return usuario.getNombre();
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
    public String getEmail() {
        return usuario.getEmail();
    }


}
