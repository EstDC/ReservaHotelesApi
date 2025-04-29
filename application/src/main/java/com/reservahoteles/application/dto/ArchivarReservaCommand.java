package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ArchivarReservaCommand", description="Comando para archivar reserva")
public record ArchivarReservaCommand(@NotNull Long idReserva,
                                     @NotNull Long idAdmin,   // identificador del actor
                                     @NotNull Rol rolActor)  // debe ser ADMINISTRADOR
{ }