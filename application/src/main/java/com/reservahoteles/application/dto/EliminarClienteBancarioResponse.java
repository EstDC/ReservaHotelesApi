package com.reservahoteles.application.dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="EliminarClienteBancarioResponse", description="Respuesta para eliminar cliente bancario")
public record EliminarClienteBancarioResponse(
        String status,    // "SUCCESS" / "FAILURE"
        String mensaje
) {}