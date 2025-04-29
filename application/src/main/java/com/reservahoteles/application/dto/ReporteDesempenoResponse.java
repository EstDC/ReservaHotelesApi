package com.reservahoteles.application.dto;

import java.time.LocalDate;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ReporteDesempenoResponse", description="Respuesta para reporte desempeno")
public record ReporteDesempenoResponse(
        LocalDate fechaInicio,
        LocalDate fechaFin,
        List<DesempenoHotelDTO> datos,
        String status,
        String mensaje                      // Ejemplo: "Marzo 2025"
) { }