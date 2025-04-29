package com.reservahoteles.application.dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="RecuperarContrasenaResponse", description="Respuesta para recuperar contrasena")
public record RecuperarContrasenaResponse(
        String status,    // "SUCCESS" / "FAILURE"
        String message    // Mensaje para el front (simula alert)
) { }