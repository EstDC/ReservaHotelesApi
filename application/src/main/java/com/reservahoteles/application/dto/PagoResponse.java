package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.EstadoPago;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(name="PagoResponse", description="Respuesta para pago")
public record PagoResponse(
        Long idPago,
        Long idReserva,
        Long idCliente,
        EstadoPago estadoPago,
        LocalDateTime fechaPago,
        BigDecimal monto,
        LocalDateTime ultimaActualizacion
) { }