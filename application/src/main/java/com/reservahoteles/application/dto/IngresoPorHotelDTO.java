package com.reservahoteles.application.dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="IngresoPorHotelDTO", description="Ingreso por hotel dto")
public record IngresoPorHotelDTO(
        Long idHotel,
        String nombreHotel,
        Double ingresos
) { }