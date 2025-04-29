package com.reservahoteles.application.dto;
import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="RegistrarServicioCommand", description="Comando para registrar servicio")
public record RegistrarServicioCommand(
        @NotBlank String nombre,
        @NotBlank String descripcion,
        @NotNull  Long   idActor,
        Rol    rolActor
) { }