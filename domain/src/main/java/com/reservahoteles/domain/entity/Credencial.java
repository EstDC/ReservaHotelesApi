package com.reservahoteles.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Credencial {
    private Long id;
    private Long idCliente;        // Relación 1 a 1 con Cliente
    private String username;
    private String passwordHash;        // Almacenar el hash de la contraseña
    private LocalDateTime fechaCreacion;
    private LocalDateTime ultimaActualizacion;
    // Opcionalmente, un campo para 'salt' si se usa

    // Para bloqueo
    @Builder.Default
    private int failedAttempts     = 0;
    @Builder.Default
    private boolean accountNonLocked = true;
    private LocalDateTime lockTime;
    // Para desactivar
    @Builder.Default
    private boolean activo = true;
    // Para recuperación de contraseña
    private String resetToken;
    private LocalDateTime resetTokenExpiry;
}