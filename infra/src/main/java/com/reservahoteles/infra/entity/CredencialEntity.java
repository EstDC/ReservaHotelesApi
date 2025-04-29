package com.reservahoteles.infra.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Audited
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "credenciales")
public class CredencialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Relación 1 a 1 con Cliente: se puede modelar por FK
    @Column(name = "id_cliente", unique = true, nullable = false)
    private Long idCliente;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    /* Opcional: columna para salt, si lo requieres
     @Column(name = "salt")
     private String salt;*/

    //Para el bloqueo
    @Column(name="failed_attempts", nullable=false)
    private int failedAttempts;

    @Column(name="account_non_locked", nullable=false)
    private boolean accountNonLocked;

    @Column(name="lock_time")
    private LocalDateTime lockTime;

    /**
     * Flag para soft‑delete / desactivación de credenciales.
     * Por defecto true (activo).
     */
    @Column(nullable = false)
    private boolean activo;

    // ── recuperación ───────────────────────────────────────────────────
    @Column(name = "reset_token", length = 100)
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiry;
}