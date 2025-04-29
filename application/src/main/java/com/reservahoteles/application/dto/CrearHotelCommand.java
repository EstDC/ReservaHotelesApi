package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="CrearHotelCommand", description="Comando para crear hotel")
public record CrearHotelCommand(
        @NotBlank String nombre,
        @NotBlank String direccion,
        @NotBlank String pais,
        @NotBlank String ciudad,
        @NotNull Double latitud,
        @NotNull Double longitud,
        @NotNull Integer numeroEstrellas,
        @NotBlank String telefono,
        @Email @NotBlank String email,
        @NotNull Long idActor,
        Rol rolActor
) { }