package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="EliminarClienteBancarioCommand", description="Comando para eliminar cliente bancario")
public record EliminarClienteBancarioCommand(
        @NotNull Long idFormaPago,
        @NotNull Long idActor,
        Rol rolActor
) { }