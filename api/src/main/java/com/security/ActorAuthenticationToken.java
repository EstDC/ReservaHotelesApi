package com.security;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class ActorAuthenticationToken extends AbstractAuthenticationToken {
    private final Long actorId;

    public ActorAuthenticationToken(Long actorId, String rol) {
        super(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol)));
        this.actorId = actorId;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null; // no usamos credenciales aquí
    }

    @Override
    public Object getPrincipal() {
        return actorId;
    }
}