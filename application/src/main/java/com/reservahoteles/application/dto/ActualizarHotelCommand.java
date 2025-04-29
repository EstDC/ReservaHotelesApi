package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name="ActualizarHotelCommand", description="Comando para actualizar hotel")
public record ActualizarHotelCommand(
        @NotNull Long   idHotel,
        @NotNull Rol rolSolicitante,      // Debe ser ADMINISTRADOR
        @NotBlank String nombre,              // opcional
        @NotBlank String direccion,           // opcional
        @NotBlank String ciudad,              // opcional
        @NotBlank String pais,                // opcional
        @NotNull Integer numeroEstrellas,    // opcional (1‑5)
        @NotBlank String telefono,            // opcional
        @Email @NotBlank String email,               // opcional
        @NotNull Double latitud,             // opcional (‑90..90)
        @NotNull Double longitud,            // opcional (‑180..180)
        List<Long> idsServicios     // opcional (lista completa a reemplazar)
) { }