package com.reservahoteles.application.dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="HotelEstadisticaDTO", description="Hotel estadistica dto")
public record HotelEstadisticaDTO(
        Long hotelId,
        String nombreHotel,
        long reservas
) { }