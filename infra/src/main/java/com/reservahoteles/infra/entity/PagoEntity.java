package com.reservahoteles.infra.entity;


import com.reservahoteles.common.enums.EstadoPago;
import com.reservahoteles.common.enums.FormaPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Audited
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pagos")
public class PagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;

    @Column(name = "id_reserva", nullable = false)
    private Long idReserva;

    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false,
            columnDefinition = "enum('PENDIENTE','COMPLETADO','RECHAZADO') default 'PENDIENTE'")
    private EstadoPago estadoPago;

    @Column(name = "otros_detalles")
    private String otrosDetalles;

    // ─── Campos nuevos para método de pago ficticio ───────────────────────
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago", nullable = false,
            columnDefinition = "enum('TARJETA','TRANSFERENCIA') default 'TARJETA'")
    private FormaPago tipoPago;

    /** Sólo el sufijo de la cuenta/tarjeta */
    @Column(name = "numero_cuenta", length = 50)
    private String numeroCuenta;

    /** Nombre que aparece en la tarjeta o instrucciones de transferencia */
    @Column(name = "titular", length = 100)
    private String titular;

    /** Formato MM/AA */
    @Column(name = "expiracion", length = 10)
    private String expiracion;

    // ─── Timestamps ────────────────────────────────────────────────────────
    @CreationTimestamp
    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @UpdateTimestamp
    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    /** Soft‑delete flag */
    @Column(name = "activo", nullable = false)
    private boolean activo;

    // ─── Para el flujo de “recuperar contraseña” ───────────────────────────
    @Column(name = "token_recupero", length = 64)
    private String resetToken;

    @Column(name = "token_expiracion")
    private LocalDateTime resetTokenExpiry;

}