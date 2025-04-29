package com.reservahoteles.application.dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="RegistrarClienteBancarioResponse", description="Respuesta para registrar cliente bancario")
public record RegistrarClienteBancarioResponse(
        Long   idFormaPago,
        String status,    // "SUCCESS" / "FAILURE"
        String mensaje
) { }
