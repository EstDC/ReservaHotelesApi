package com.reservahoteles.application.dto;
import io.swagger.v3.oas.annotations.media.Schema;
@Schema(name="ActualizarClienteResponse", description="Respuesta para actualizar cliente")
public record ActualizarClienteResponse(
        Long idCliente,
        String status,
        String mensaje
) { }