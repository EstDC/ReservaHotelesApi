package com.reservahoteles.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="EliminarExtraResponse", description="Respuesta para eliminar extra")
public record EliminarExtraResponse(
        Long   idExtra,
        String status,
        String mensaje
) {}