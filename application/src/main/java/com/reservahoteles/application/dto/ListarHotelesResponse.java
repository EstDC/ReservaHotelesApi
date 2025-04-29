package com.reservahoteles.application.dto;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ListarHotelesResponse", description="Respuesta para listar hoteles")
public record ListarHotelesResponse(
        Long idHotel,
        String nombre,
        String direccion,
        String pais,
        String ciudad,
        Double latitud,
        Double longitud,
        Integer numeroEstrellas,
        String telefono,
        String email,
        LocalDateTime fechaRegistro,
        LocalDateTime ultimaActualizacion
) {}