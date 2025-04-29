package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

@Schema(name="CrearReservaCommand", description="Comando para crear reserva")
public record CrearReservaCommand(
        @NotNull Long idCliente,
        @NotNull Long idHabitacion,
        @NotNull LocalDate fechaEntrada,
        @NotNull LocalDate fechaSalida,
        List<Long> idsExtras,
        @NotNull Long idActor,
        Rol rolActor
) { }