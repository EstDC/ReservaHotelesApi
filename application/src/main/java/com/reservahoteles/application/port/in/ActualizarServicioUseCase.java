package com.reservahoteles.application.port.in;
import com.reservahoteles.application.dto.ActualizarServicioCommand;
import com.reservahoteles.application.dto.ActualizarServicioResponse;

public interface ActualizarServicioUseCase {
    ActualizarServicioResponse ejecutar(ActualizarServicioCommand cmd);
}