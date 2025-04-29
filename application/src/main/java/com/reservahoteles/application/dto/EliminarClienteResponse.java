package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="EliminarClienteResponse", description="Respuesta para eliminar cliente")
public record EliminarClienteResponse(
        Long idCliente,
        String status,
        String mensaje
) { }