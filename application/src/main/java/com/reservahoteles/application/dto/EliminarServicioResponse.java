package com.reservahoteles.application.dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="EliminarServicioResponse", description="Respuesta para eliminar servicio")
public record EliminarServicioResponse(
        Long   idServicio,
        String status,
        String mensaje
) { }