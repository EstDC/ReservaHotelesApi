package com.reservahoteles.application.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ConfirmarRecuperarContrasenaCommand", description="Comando para confirmar recuperar contrasena")
public record ConfirmarRecuperarContrasenaCommand(
        @NotBlank String token,
        @NotBlank String newPassword,
        String confirmPassword
) { }