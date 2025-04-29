package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ConfirmarRecuperarContrasenaResponse", description="Respuesta para confirmar recuperar contrasena")
public record ConfirmarRecuperarContrasenaResponse(
        Long idCliente,
        String status,    // "SUCCESS" o "FAILURE"
        String mensaje
) { }