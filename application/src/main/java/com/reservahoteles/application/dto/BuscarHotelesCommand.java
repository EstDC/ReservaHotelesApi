package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="BuscarHotelesCommand", description="Comando para buscar hoteles")
public record BuscarHotelesCommand(
        // filtros opcionales
        String pais,
        String ciudad,
        Integer numeroEstrellas,
        String nombreParcial,
        String direccionParcial,
        String telefonoParcial,
        String emailParcial,

        // control de acceso
        @NotNull Long   idActor,
        @NotNull Rol rolActor,

        // paginación
        @Min(0) int     page,
        @Min(1) int     size
) {}