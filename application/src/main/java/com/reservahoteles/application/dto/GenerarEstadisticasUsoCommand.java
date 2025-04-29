package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="GenerarEstadisticasUsoCommand", description="Comando para generar estadisticas uso")
public record GenerarEstadisticasUsoCommand(
        @NotNull LocalDate fechaInicio,
        @NotNull LocalDate fechaFin,
        @NotNull Long idActor,
        Rol rolActor
) { }