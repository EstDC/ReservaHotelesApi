package com.reservahoteles.application.dto;
import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ActualizarServicioCommand", description="Comando para actualizar servicio")
public record ActualizarServicioCommand(
        @NotNull Long   idServicio,
        @NotBlank String nombre,
        @NotBlank String descripcion,
        @NotNull Long   idActor,
        Rol    rolActor
) { }