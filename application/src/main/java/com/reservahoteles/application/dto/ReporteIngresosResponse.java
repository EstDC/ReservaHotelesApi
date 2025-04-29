package com.reservahoteles.application.dto;

import java.time.LocalDate;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ReporteIngresosResponse", description="Respuesta para reporte ingresos")
public record ReporteIngresosResponse(
        LocalDate fechaInicio,
        LocalDate fechaFin,
        List<IngresoPorHotelDTO> ingresosPorHotel,
        Double totalIngresos,
        String status,     // "SUCCESS" o "FAILURE"
        String mensaje
) { }