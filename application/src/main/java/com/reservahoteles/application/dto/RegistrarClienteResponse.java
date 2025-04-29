package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="RegistrarClienteResponse", description="Respuesta para registrar cliente")
public record RegistrarClienteResponse(
        Long idCliente,
        String status,  // Por ejemplo: "SUCCESS" o "FAILURE"
        String mensaje  // Mensaje informativo
) { }