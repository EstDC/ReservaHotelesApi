package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="EliminarServicioCommand", description="Comando para eliminar servicio")
public record EliminarServicioCommand(
        @NotNull Long idServicio,
        @NotNull Long idActor,
        Rol rolActor
) { }