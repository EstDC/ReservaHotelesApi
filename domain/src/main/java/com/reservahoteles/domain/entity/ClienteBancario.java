package com.reservahoteles.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteBancario {
    private Long id;
    private Long idCliente; // Referencia al cliente (puedes optar por una agregación o solo el identificador)
    private String banco;
    private String numeroCuenta;
    private String otrosDetalles;
}
