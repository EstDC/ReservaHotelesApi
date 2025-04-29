package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="EliminarHabitacionCommand", description="Comando para eliminar habitacion")
public record EliminarHabitacionCommand(
        @NotNull Long idHabitacion,
        @NotNull Long idActor,
        Rol rolActor
) { }