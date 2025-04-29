package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ArchivarReservaResponse", description="Respuesta para archivar reserva")
public record ArchivarReservaResponse(
        Long idReserva,
        String status,   // Ejemplo: "SUCCESS" o "FAILURE"
        String mensaje
) { }