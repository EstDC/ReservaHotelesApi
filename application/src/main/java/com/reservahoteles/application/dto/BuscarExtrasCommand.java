package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * Comando para buscar extras.
 * - Si idExtra != null, devuelve sólo ese extra.
 * - Si idExtra == null:
 *    - admin → todos
 *    - cliente → extras de la habitación idHabitacion (requerido).
 */
@Schema(name="BuscarExtrasCommand", description="Comando para buscar extras")
public record BuscarExtrasCommand(
        Long    idExtra,
        Long    idHabitacion,
        String  nombre,
        @NotNull Long   idActor,
        @NotNull Rol    rolActor,
        int     page,
        int     size
) {}