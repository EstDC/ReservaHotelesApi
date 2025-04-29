package com.reservahoteles.application.dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="SolicitarExtrasReservaResponse", description="Respuesta para solicitar extras reserva")
public record SolicitarExtrasReservaResponse(
        Long idReserva,
        String status,   // SUCCESS | FAILURE
        String mensaje
) { }