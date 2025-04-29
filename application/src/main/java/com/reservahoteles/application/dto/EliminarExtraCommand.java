package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="EliminarExtraCommand", description="Comando para eliminar extra")
public record EliminarExtraCommand(
        @NotNull Long idExtra,
        @NotNull Long idActor,
        @NotNull Rol rolActor
) {}