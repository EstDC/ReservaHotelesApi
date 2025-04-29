package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.EstadoReserva;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="HistorialReservaResponse", description="Respuesta para historial reserva")
public record HistorialReservaResponse(
        Long idHistorial,
        Long idReserva,
        Long idCliente,
        Long idHabitacion,
        LocalDate fechaEntrada,
        LocalDate fechaSalida,
        EstadoReserva estadoReserva,
        LocalDateTime fechaReserva,
        Double total,
        LocalDateTime fechaArchivo  // Fecha en la que se archivó la reserva
) { }