package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="AsignarExtrasHabitacionResponse", description="Respuesta para asignar extras habitacion")
public record AsignarExtrasHabitacionResponse(
        Long idHabitacion,
        String status,
        String mensaje
) { }