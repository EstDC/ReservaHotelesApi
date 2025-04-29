package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(name="GenerarReporteIngresosCommand", description="Comando para generar reporte ingresos")
public record GenerarReporteIngresosCommand(
        @NotNull LocalDate fechaInicio,
        @NotNull LocalDate fechaFin,
        @NotNull Long idHotel,      // opcional, filtrar solo un hotel
        @NotNull Long idActor,      // quién solicita
        Rol rolActor       // para validación de permisos
) { }