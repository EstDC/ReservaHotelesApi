package com.reservahoteles.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ActualizarCredencialesCommand", description="Comando para actualizar credenciales")
public record ActualizarCredencialesCommand(
        @NotNull Long idCliente,            // Identificador del cliente al que pertenecen las credenciales
        @NotBlank String username,           // Nuevo username (opcional)
        @NotBlank String currentPassword,    // Contraseña actual (requerida si se quiere cambiar contraseña)
        @NotBlank String newPassword,        // Nueva contraseña (opcional)
        String confirmPassword     // Confirmación de nueva contraseña (opcional)
) { }