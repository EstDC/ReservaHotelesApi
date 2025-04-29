package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ConsultarHistorialHabitacionCommand", description="Comando para consultar historial habitacion")
public record ConsultarHistorialHabitacionCommand(
        Long idHabitacion,
        int page,      // número de página (desde 0)
        int size
) { }