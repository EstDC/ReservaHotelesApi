package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resultado de intentar notificar un cambio de reserva.
 */
@Schema(name="NotificarCambiosReservasResponse", description="Respuesta para notificar cambios reservas")
public record NotificarCambiosReservasResponse(
        Long idReserva,
        String status,       // "SUCCESS" o "FAILURE"
        String mensaje       // mensaje de explicación
) { }