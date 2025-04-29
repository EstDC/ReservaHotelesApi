package com.reservahoteles.application.dto;
import io.swagger.v3.oas.annotations.media.Schema;
@Schema(name="ActualizarServicioResponse", description="Respuesta para actualizar servicio")
public record ActualizarServicioResponse(
        Long   idServicio,
        String status,
        String mensaje
) { }