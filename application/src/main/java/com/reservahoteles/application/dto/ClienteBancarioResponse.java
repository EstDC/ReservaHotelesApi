package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.FormaPago;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(name="ClienteBancarioResponse", description="Respuesta para cliente bancario")
public record ClienteBancarioResponse(
        Long      idFormaPago,
        FormaPago tipoPago,
        String    numeroCuenta,
        String    titular,
        String    expiracion,
        String    otrosDetalles,
        LocalDateTime fechaRegistro,
        LocalDateTime ultimaActualizacion,
        boolean   activo
) { }