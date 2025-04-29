package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ActualizarCredencialesResponse", description="Respuesta para actualizar credenciales")
public record ActualizarCredencialesResponse(
        Long idCliente,
        String status,
        String mensaje
) { }