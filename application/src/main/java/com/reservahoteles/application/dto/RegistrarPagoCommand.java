package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.FormaPago;
import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="RegistrarPagoCommand", description="Comando para registrar pago")
public record RegistrarPagoCommand(
        @NotNull Long   idReserva,
        @NotNull Long   idCliente,
        @NotNull BigDecimal monto,
        @NotNull LocalDateTime fechaPago,
        @NotNull FormaPago    formaPago,
        @NotBlank String       numeroCuenta,
        @NotBlank String       titular,
        @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$") String       expiracion,
        @NotBlank String       otrosDetalles,
        @NotNull Long         idActor,
        @NotNull Rol          rolActor
){}