package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.ModoAsignacion;
import com.reservahoteles.common.enums.Rol;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="AsignarServiciosHotelCommand", description="Comando para asignar servicios hotel")
public record AsignarServiciosHotelCommand(
        @NotNull Long idHotel,
        List<Long> idsServicios,
        @NotNull ModoAsignacion modo,          // puede ser null → REEMPLAZAR
        @NotNull Rol rolActor,                // CLIENTE | ADMINISTRADOR
        Long idActor                  // id del usuario que invoca
) { }