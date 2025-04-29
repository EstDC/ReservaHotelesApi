package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="EliminarHabitacionResponse", description="Respuesta para eliminar habitacion")
public record EliminarHabitacionResponse(
        Long idHabitacion,
        String status,
        String mensaje
) { }