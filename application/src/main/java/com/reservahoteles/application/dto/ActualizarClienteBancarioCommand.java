package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.FormaPago;
import com.reservahoteles.common.enums.Rol;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(name="ActualizarClienteBancarioCommand", description="Comando para actualizar cliente bancario")
public record ActualizarClienteBancarioCommand(
        @NotNull Long   idFormaPago,
        @NotNull FormaPago tipoPago,
        @NotBlank String numeroCuenta,
        @NotBlank String titular,
        @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$") String expiracion,
        @NotBlank String otrosDetalles,
        @NotNull Long   idActor,
        Rol rolActor
) { }