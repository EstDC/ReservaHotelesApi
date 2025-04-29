package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="CancelarReservaCommand", description="Comando para cancelar reserva")
public record CancelarReservaCommand(
        @NotNull Long idReserva,
        @NotNull Long idActor,
        Rol rolActor
) {}