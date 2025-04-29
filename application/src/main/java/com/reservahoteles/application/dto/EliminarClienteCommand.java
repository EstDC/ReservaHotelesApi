package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="EliminarClienteCommand", description="Comando para eliminar cliente")
public record EliminarClienteCommand(
        @NotNull Long idCliente,
        @NotNull Long idActor,
        Rol rolActor
) { }