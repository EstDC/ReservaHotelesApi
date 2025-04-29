package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ActualizarEstadoPagoResponse", description="Respuesta para actualizar estado pago")
public record ActualizarEstadoPagoResponse(
        Long idPago,
        String status,
        String mensaje
) { }