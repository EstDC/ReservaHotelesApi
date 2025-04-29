package com.reservahoteles.application.dto;


import com.reservahoteles.common.enums.EstadoPago;
import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(name="ConsultarPagosCommand", description="Comando para consultar pagos")
public record ConsultarPagosCommand(
        Long idPago,                  // Opcional: si lo pones solo devolverá ese pago
        Long idReserva,               // Opcional: filtrar por reserva
        Long idCliente,               // Opcional: filtrar por cliente (admin) / siempre el propio (cliente)
        LocalDateTime fechaInicio,    // Opcional: fecha pago >=
        LocalDateTime fechaFin,       // Opcional: fecha pago <=
        EstadoPago estadoPago,        // Opcional: filtrar por estado
        @NotNull Long idActor,                 // ID del que invoca
        Rol rolActor,                  // Rol del que invoca
        int page,      // número de página (desde 0)
        int size
) { }