package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="AsignarServiciosHotelResponse", description="Respuesta para asignar servicios hotel")
public record AsignarServiciosHotelResponse(
        Long idHotel,
        String status,
        String mensaje
) { }