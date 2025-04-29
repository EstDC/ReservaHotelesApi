package com.reservahoteles.application.dto;

import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.common.enums.TipoIdentificacion;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(name="BuscarClientesResponse", description="Respuesta para buscar clientes")
public record BuscarClientesResponse(
        Long idCliente,
        String nombre,
        String email,
        String telefono,
        String direccion,
        TipoIdentificacion tipoIdentificacion,
        String identificacion,
        LocalDateTime fechaRegistro,
        LocalDateTime ultimaActualizacion,
        Rol rol
) {}