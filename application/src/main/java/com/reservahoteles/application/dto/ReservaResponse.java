package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.EstadoReserva;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ReservaResponse", description="Respuesta para reserva")
public record ReservaResponse(
        Long idReserva,
        Long idCliente,
        Long idHabitacion,
        LocalDate fechaEntrada,
        LocalDate fechaSalida,
        EstadoReserva estadoReserva,
        LocalDateTime fechaReserva,
        Double total,
        LocalDateTime ultimaActualizacion,
        List<Long> extrasIds   // opcional, devolver sólo IDs de extras asociados
) {}