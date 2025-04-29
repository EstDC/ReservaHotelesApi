package com.reservahoteles.application.dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="ActualizarClienteBancarioResponse", description="Respuesta para actualizar cliente bancario")
public record ActualizarClienteBancarioResponse(
        Long idFormaPago,
        String status,    // "SUCCESS" / "FAILURE"
        String mensaje
) {}