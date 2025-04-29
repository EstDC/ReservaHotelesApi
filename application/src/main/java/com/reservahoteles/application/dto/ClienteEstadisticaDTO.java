package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ClienteEstadisticaDTO", description="Cliente estadistica dto")
public record ClienteEstadisticaDTO(
        Long clienteId,
        String nombreCliente,
        long reservas
) { }