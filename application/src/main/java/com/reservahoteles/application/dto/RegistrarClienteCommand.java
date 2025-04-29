package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.TipoIdentificacion;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="RegistrarClienteCommand", description="Comando para registrar cliente")
public record RegistrarClienteCommand(
        @NotBlank String nombre,
        @Email @NotBlank String email,
        @NotBlank String telefono,
        @NotBlank String direccion,
        @NotNull TipoIdentificacion tipoIdentificacion,
        @NotBlank String identificacion,
        @NotBlank String username,
        @NotBlank String password
) { }