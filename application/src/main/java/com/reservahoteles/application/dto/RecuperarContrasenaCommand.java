package com.reservahoteles.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="RecuperarContrasenaCommand", description="Comando para recuperar contrasena")
public record RecuperarContrasenaCommand(
        @Email @NotBlank String email,
        @NotBlank String token,
        @NotBlank String newPassword,
        @NotBlank String confirmPassword
) { }