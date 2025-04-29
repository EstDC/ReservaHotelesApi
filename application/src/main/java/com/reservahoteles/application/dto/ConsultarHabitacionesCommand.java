package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.EstadoHabitacion;
import com.reservahoteles.common.enums.TipoHabitacion;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name="ConsultarHabitacionesCommand", description="Comando para consultar habitaciones")
public record ConsultarHabitacionesCommand(
        @NotNull Long idHotel,
        EstadoHabitacion estado,    // Opcional
        TipoHabitacion tipo,        // Opcional
        Long capacidadMin,          // Opcional: capacidad mínima
        Long capacidadMax,          // Opcional: capacidad máxima
        BigDecimal precioMin,       // Opcional: precio mínimo por noche
        BigDecimal precioMax,       // Opcional: precio máximo por noche
        int page,                   // número de página (desde 0)
        int size
) { }