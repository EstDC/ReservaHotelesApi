package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(name="ConsultarHistorialClienteCommand", description="Comando para consultar historial cliente")
public record ConsultarHistorialClienteCommand(
        @NotNull Long idCliente,
        LocalDate fechaInicio,  // opcional
        LocalDate fechaFin,     // opcional
        @NotNull Long idActor,
        Rol rolActor
) {}