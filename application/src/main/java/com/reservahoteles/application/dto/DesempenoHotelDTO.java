package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="DesempenoHotelDTO", description="Desempeno hotel dto")
public record DesempenoHotelDTO(
        Long idHotel,
        String nombreHotel,
        long totalReservas,
        long confirmadas,
        long canceladas,
        double ingresoTotal,
        double duracionPromedioNoches,
        double tasaOcupacion   // % de noches ocupadas
) { }