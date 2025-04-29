package com.reservahoteles.application.dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="RegistrarHabitacionResponse", description="Respuesta para registrar habitacion")
public record RegistrarHabitacionResponse(
        Long idHabitacion,
        String status,   // "SUCCESS" o "FAILURE"
        String mensaje
) { }