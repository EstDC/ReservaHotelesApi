package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="EliminarHotelResponse", description="Respuesta para eliminar hotel")
public record EliminarHotelResponse(
        Long idHotel,
        String status,
        String mensaje
) { }