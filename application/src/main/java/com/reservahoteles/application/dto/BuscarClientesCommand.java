package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.common.enums.TipoIdentificacion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name="BuscarClientesCommand", description="Comando para buscar clientes")
public record BuscarClientesCommand(
        Long id,                          // opcional: buscar por id
        String nombre,                    // opcional
        String email,                     // opcional
        String telefono,                  // opcional
        String direccion,                 // opcional
        TipoIdentificacion tipoIdentificacion, // opcional
        String identificacion,            // opcional

        @NotNull Long idActor,            // obligatorio: quién llama
        @NotNull Rol rolActor,            // obligatorio: rol de quién llama
        int page,                         // paginación
        int size
) {}