package com.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.reservahoteles.common.enums.Rol;

import java.io.IOException;

public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        String idActor = req.getHeader("X-Actor-Id");
        String rolActor = req.getHeader("X-Actor-Rol");
        if (idActor != null && rolActor != null) {
            try {
                Long actorId = Long.valueOf(idActor);
                // validar que rolActor coincide con tu enum
                Rol.valueOf(rolActor);
                var auth = new ActorAuthenticationToken(actorId, rolActor);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception ignored) {
                // cabecera inválida: no autenticamos
            }
        }

        chain.doFilter(req, res);
    }
}