package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="BuscarServiciosCommand", description="Comando para buscar servicios")
public record BuscarServiciosCommand(
        Long   idServicio,      // opcional: consulta por ID
        String nombreParcial,   // opcional: busca LIKE %valor%
        @NotNull Long   idActor,
        @NotNull Rol    rolActor,
        @Min(0)    int  page,
        @Min(1)    int  size
) { }