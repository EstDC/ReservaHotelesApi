package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="EliminarCredencialesResponse", description="Respuesta para eliminar credenciales")
public record EliminarCredencialesResponse(
        Long idCliente,
        String status,
        String mensaje
) { }