package com.reservahoteles.application.dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="RegistrarPagoResponse", description="Respuesta para registrar pago")
public record RegistrarPagoResponse(
        Long idPago,
        String status,   // Ejemplo: "SUCCESS" o "FAILURE"
        String mensaje
) { }