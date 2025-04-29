package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.EstadoHabitacion;
import com.reservahoteles.common.enums.TipoHabitacion;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name="HabitacionResponse", description="Respuesta para habitacion")
public record HabitacionResponse(
        Long idHabitacion,
        String numeroHabitacion,
        TipoHabitacion tipo,
        Long capacidad,
        BigDecimal precioPorNoche,
        EstadoHabitacion estado,
        String descripcion
) { }