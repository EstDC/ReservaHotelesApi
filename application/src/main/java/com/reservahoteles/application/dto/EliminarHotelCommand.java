package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * Command para eliminar un hotel.
 * Solo un administrador puede invocar este caso de uso.
 */
@Schema(name="EliminarHotelCommand", description="Comando para eliminar hotel")
public record EliminarHotelCommand(
        @NotNull Long idHotel,
        @NotNull Long idActor,
        Rol rolActor
) { }