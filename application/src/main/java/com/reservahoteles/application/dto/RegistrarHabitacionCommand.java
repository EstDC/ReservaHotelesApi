package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.EstadoHabitacion;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.common.enums.TipoHabitacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name="RegistrarHabitacionCommand", description="Comando para registrar habitacion")
public record RegistrarHabitacionCommand(
        @NotNull Long idHotel,
        @NotBlank String numeroHabitacion,
        @NotNull TipoHabitacion tipo,
        @NotNull Long capacidad,
        @NotNull BigDecimal precioPorNoche,
        @NotNull EstadoHabitacion estado,
        @NotBlank String descripcion,
        @NotNull Long idActor,
        @NotNull Rol rolActor
) {}