package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name="ActualizarEstadoPagoCommand", description="Comando para actualizar estado pago")
public record ActualizarEstadoPagoCommand(
        @NotNull Long idPago,
        String formaPago // "TARJETA" o "TRANSFERENCIA"
) {}