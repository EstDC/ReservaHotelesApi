package com.reservahoteles.application.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="AutenticarClienteCommand", description="Comando para autenticar cliente")
public record AutenticarClienteCommand(
        @NotBlank String username,
        String password
) { }