package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.EstadoHabitacion;
import com.reservahoteles.common.enums.TipoHabitacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name="ActualizarHabitacionCommand", description="Comando para actualizar habitacion")
public record ActualizarHabitacionCommand(
        @NotNull Long idHabitacion,
        @NotNull Long idHotel,                  // para moverla a otro hotel
        @NotBlank String numeroHabitacion,       // único por hotel
        @NotNull TipoHabitacion tipo,                   // ej. "SINGLE", "SUITE" …
        @NotNull Long capacidad,
        @NotNull BigDecimal precioPorNoche,
        @NotNull EstadoHabitacion estado,       // DISPONIBLE | OCUPADA | MANTENIMIENTO
        @NotBlank String descripcion,
        List<Long> idsExtras
) { }