package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ActualizarHabitacionResponse", description="Respuesta para actualizar habitacion")
public record ActualizarHabitacionResponse(
        Long idHabitacion,
        String status,
        String mensaje
) { }