package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="CrearHotelResponse", description="Respuesta para crear hotel")

public record CrearHotelResponse(
        Long idHotel,
        String status,  // Ejemplo: "SUCCESS" o "FAILURE"
        String mensaje
) { }