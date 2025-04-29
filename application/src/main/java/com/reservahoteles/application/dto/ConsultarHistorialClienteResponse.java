package com.reservahoteles.application.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ConsultarHistorialClienteResponse", description="Respuesta para consultar historial cliente")
public record ConsultarHistorialClienteResponse(
        Long idCliente,
        String status,                     // "SUCCESS" o "FAILURE"
        String mensaje,                    // "Historial consultado correctamente", etc.
        List<HistorialReservaResponse> historial
) { }