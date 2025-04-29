package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(name="GenerarReporteDesempenoCommand", description="Comando para generar reporte desempeno")
public record GenerarReporteDesempenoCommand(
        @NotNull LocalDate fechaInicio,
        @NotNull LocalDate fechaFin,
        @NotNull Long idHotel,       // opcional: si es null, reporta para todos
        @NotNull Long idActor,
        Rol rolActor
) { }