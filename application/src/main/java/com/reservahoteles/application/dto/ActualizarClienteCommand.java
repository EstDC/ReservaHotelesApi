package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.common.enums.TipoIdentificacion;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ActualizarClienteCommand", description="Comando para actualizar cliente")
public record ActualizarClienteCommand(
        @NotNull                 Long   idCliente,
        String                   nombre,            // opcional
        @Email                    String email,       // opcional
        String                   telefono,          // opcional
        String                   direccion,         // opcional
        TipoIdentificacion       tipoIdentificacion,// opcional
        String                   identificacion,    // opcional
        @NotNull                 Long   idActor,
        @NotNull                 Rol    rolActor
) { }