package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="CrearReservaResponse", description="Respuesta para crear reserva")
public record CrearReservaResponse(
        Long idReserva,
        String status,  // Ejemplo: "SUCCESS" o "FAILURE"
        String mensaje
) { }