package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="AutenticarClienteResponse", description="Respuesta para autenticar cliente")
public record AutenticarClienteResponse(
        Long idCliente,
        String token,     // Por ejemplo, token JWT
        String rol,       // "CLIENTE" o "ADMINISTRADOR"
        String status,    // "SUCCESS" o "FAILURE"
        String mensaje
) { }