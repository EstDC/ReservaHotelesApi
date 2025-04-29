package com.reservahoteles.application.dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="RegistrarExtraResponse", description="Respuesta para registrar extra")
public record RegistrarExtraResponse(
        Long   idExtra,
        String status,   // "SUCCESS"/"FAILURE"
        String mensaje
) {}