package com.reservahoteles.application.dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ServicioResponse", description="Respuesta para servicio")
public record ServicioResponse(
        Long idServicio,
        String nombre,
        String descripcion
) { }