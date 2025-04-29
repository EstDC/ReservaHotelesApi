package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(
        name = "EliminarCredencialesCommand",
        description = "Comando para eliminar las credenciales de un actor determinado"
)
public record EliminarCredencialesCommand(

        @NotNull
        @Schema(
                description = "Identificador de la credencial a eliminar",
                example = "123"
        )
        Long idCredencial,

        @NotNull
        @Schema(
                description = "Identificador del actor que realiza la operación",
                example = "42"
        )
        Long idActor,

        @Schema(
                description = "Rol del actor que solicita la eliminación",
                example = "ADMIN",
                allowableValues = {"ADMIN","USER","GUEST"}
        )
        Rol rolActor

) { }