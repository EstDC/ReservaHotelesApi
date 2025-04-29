package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ActualizarReservaResponse", description="Respuesta para actualizar reserva")
public record ActualizarReservaResponse(
        Long idReserva,
        String status,
        String mensaje
) { }