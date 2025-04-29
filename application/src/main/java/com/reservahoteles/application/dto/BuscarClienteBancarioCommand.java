package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="BuscarClienteBancarioCommand", description="Comando para buscar cliente bancario")
public record BuscarClienteBancarioCommand(
        Long idFormaPago,           // opcional: si viene, devolvemos solo ese registro
        @NotNull Long idActor,      // quién hace la llamada
        @NotNull Rol rolActor,      // su rol
        int page,                   // página (desde 0)
        int size                    // tamaño de página
) { }