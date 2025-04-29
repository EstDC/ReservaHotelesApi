package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.FormaPago;
import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="RegistrarClienteBancarioCommand", description="Comando para registrar cliente bancario")
public record RegistrarClienteBancarioCommand(
        @NotNull Long idCliente,
        @NotNull FormaPago tipoPago,
        @NotBlank String numeroCuenta,  // solo sufijo
        @NotBlank String titular,
        @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$") String expiracion,    // MM/AA
        @NotBlank String otrosDetalles,
        @NotNull Long   idActor,
        @NotNull Rol rolActor
) { }
