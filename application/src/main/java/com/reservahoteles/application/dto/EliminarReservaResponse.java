package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="EliminarReservaResponse", description="Respuesta para eliminar reserva")
public record EliminarReservaResponse(
        Long idReserva,
        String status,
        String mensaje
) { }