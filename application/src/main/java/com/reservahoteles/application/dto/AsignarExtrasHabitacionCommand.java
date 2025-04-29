package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.ModoAsignacion;
import com.reservahoteles.common.enums.Rol;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(name="AsignarExtrasHabitacionCommand", description="Comando para asignar extras habitacion")
public record AsignarExtrasHabitacionCommand(
        @NotNull Long idHabitacion,          // habitación a modificar          (obligatorio)
        List<Long> idsExtras,       // extras que se desean asignar    (obligatorio, puede estar vacía)
        @NotNull ModoAsignacion modo,        // ADD, REMOVE o REPLACE           (obligatorio)
        @NotNull Long idActor,               // id del usuario que llama        (obligatorio)
        Rol rolActor               // CLIENTE o ADMINISTRADOR         (obligatorio)
) {}