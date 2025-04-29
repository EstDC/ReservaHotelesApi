package com.security;

import com.reservahoteles.common.enums.Rol;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() { /* no instanciable */ }

    /** Devuelve el actorId que puso el HeaderAuthenticationFilter. */
    public static Long currentActorId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new IllegalStateException("No hay actor autenticado");
        }
        return (Long) auth.getPrincipal();
    }

    /** Extrae el rol (enum Rol) de la autoridad “ROLE_XYZ”. */
    public static Rol currentActorRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().isEmpty()) {
            throw new IllegalStateException("No hay rol autenticado");
        }
        String ga = auth.getAuthorities().iterator().next().getAuthority(); // e.g. "ROLE_CLIENTE"
        return Rol.valueOf(ga.substring("ROLE_".length()));
    }
}