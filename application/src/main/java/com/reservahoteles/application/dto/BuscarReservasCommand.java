package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.*;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(name="BuscarReservasCommand", description="Comando para buscar reservas")
public record BuscarReservasCommand(
        Long idReserva,                 // opcional
        Long idCliente,                 // opcional
        Long idHotel,                   // opcional (filtro extra)
        Long idHabitacion,              // opcional
        EstadoReserva estadoReserva,    // opcional
        EstadoHabitacion estadoHabitacion, // opcional
        TipoHabitacion tipoHabitacion,  // opcional
        EstadoPago estadoPago,          // opcional
        LocalDate fechaInicio,          // opcional
        LocalDate fechaFin,             // opcional
        @NotNull Long idActor,          // obligatorio
        @NotNull Rol rolActor,          // obligatorio
        int page,                       // paginación
        int size
) {}