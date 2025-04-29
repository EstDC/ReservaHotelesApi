package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.EstadoReserva;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="CancelarReservaResponse", description="Respuesta para cancelar reserva")
public record CancelarReservaResponse(
        Long idReserva,
        EstadoReserva nuevoEstadoReserva,
        String mensaje
) { }