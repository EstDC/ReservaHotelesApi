package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

@Schema(name="ActualizarReservaCommand", description="Comando para actualizar reserva")
public record ActualizarReservaCommand(
        @NotNull Long idReserva,
        @NotNull Long idActor,                // ← quién hace la petición
        @NotNull Rol rolActor,               // CLIENTE / ADMINISTRADOR
        @NotNull LocalDate fechaEntrada,      // (solo admin)
        @NotNull LocalDate fechaSalida,       // (solo admin)
        @NotNull Long idHabitacion,           // (solo admin)
        @NotNull EstadoReserva estadoReserva, // admin: libre; cliente: solo CANCELAR
        List<Long> idsExtras         // admin y cliente
) { }