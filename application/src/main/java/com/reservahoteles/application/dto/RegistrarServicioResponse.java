package com.reservahoteles.application.dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="RegistrarServicioResponse", description="Respuesta para registrar servicio")
public record RegistrarServicioResponse(
        Long   idServicio,
        String status,    // "SUCCESS" o "FAILURE"
        String mensaje
) { }