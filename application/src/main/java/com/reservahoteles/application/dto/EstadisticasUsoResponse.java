package com.reservahoteles.application.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="EstadisticasUsoResponse", description="Respuesta para estadisticas uso")
public record EstadisticasUsoResponse(
        long totalReservas,
        long clientesUnicos,
        double ingresosTotales,
        double duracionMediaDias,
        List<HotelEstadisticaDTO> topHoteles,
        List<ClienteEstadisticaDTO> topClientes,
        String status,
        String mensaje
) { }