package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(name="ConsultarHistorialReservasCommand", description="Comando para consultar historial reservas")
public record ConsultarHistorialReservasCommand(
        Long idReserva,           // Opcional: filtrar por id de reserva
        Long idCliente,           // Opcional: filtrar por id de cliente
        Long idHabitacion,        // Opcional: filtrar por id de habitación
        LocalDate fechaInicio,    // Opcional: fecha de archivo desde (inclusive)
        LocalDate fechaFin,       // Opcional: fecha de archivo hasta (inclusive)
        EstadoReserva estadoReserva, // Opcional: filtrar por estado histórico de la reserva
        @NotNull Long idActor,             // quien invoca
        Rol rolActor,              // rol de quien invoca
        int page,      // número de página (desde 0)
        int size
) { }