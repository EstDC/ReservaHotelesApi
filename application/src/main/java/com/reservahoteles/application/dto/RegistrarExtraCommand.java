package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="RegistrarExtraCommand", description="Comando para registrar extra")
public record RegistrarExtraCommand(
        @NotBlank String nombre,
        @NotNull Double costoAdicional,
        @NotNull  Long   idActor,
        @NotNull Rol rolActor
) {}
