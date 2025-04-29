package com.reservahoteles.domain.entity;


import com.reservahoteles.common.enums.EstadoPago;
import com.reservahoteles.common.enums.FormaPago;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    private Long id;
    private Long idReserva;
    private Long idCliente;
    private BigDecimal monto;
    private LocalDateTime fechaPago;
    private EstadoPago estadoPago;
    private String otrosDetalles;

    // ← NUEVO
    private FormaPago tipoPago;
    private String numeroCuenta;
    private String titular;
    private String expiracion;
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimaActualizacion;
    private boolean activo;

    // ← Para “recuperar contraseña”
    private String resetToken;
    private LocalDateTime resetTokenExpiry;
}