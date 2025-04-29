package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
/**
 * DTO de salida para un Extra.
 */
@Schema(name="ExtraResponse", description="Respuesta para extra")
public record ExtraResponse(
        Long    id,
        String  nombre,
        double  costoAdicional
) {}