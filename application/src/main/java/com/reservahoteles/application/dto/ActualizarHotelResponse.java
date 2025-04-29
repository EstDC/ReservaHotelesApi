package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ActualizarHotelResponse", description="Respuesta para actualizar hotel")
public record ActualizarHotelResponse(
        Long idHotel,
        String status,
        String mensaje
) { }