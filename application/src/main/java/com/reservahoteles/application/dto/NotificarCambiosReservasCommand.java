package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * Encapsula la petición de notificar un cambio en la reserva.
 */
@Schema(name="NotificarCambiosReservasCommand", description="Comando para notificar cambios reservas")
public record NotificarCambiosReservasCommand(
        @NotNull Long idReserva,
        @NotNull Rol rolActor,         // opcional, para controlar permisos si lo necesitas
        Long idActor          // opcional, para saber quién dispara la notificación
) { }