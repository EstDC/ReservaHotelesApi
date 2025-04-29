package com.reservahoteles.application.dto;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="HistorialCambioDTO", description="Historial cambio dto")
public record HistorialCambioDTO(
        LocalDateTime fechaCambio,
        String descripcion   // Descripción del cambio, p.ej. "Actualizó dirección" o "Modificó teléfono"
) { }