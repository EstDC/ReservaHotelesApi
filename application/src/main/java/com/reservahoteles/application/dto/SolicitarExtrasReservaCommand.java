package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.ModoAsignacion;
import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name="SolicitarExtrasReservaCommand", description="Comando para solicitar extras reserva")
public record SolicitarExtrasReservaCommand(
        @NotNull Long idReserva,
        List<Long> idsExtras,
        @NotNull ModoAsignacion modo,
        @NotNull Long idActor,
        @NotNull Rol rolActor
) { }