package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.ActualizarReservaCommand;
import com.reservahoteles.application.dto.ActualizarReservaResponse;

public interface ActualizarReservaUseCase {
    ActualizarReservaResponse ejecutar(ActualizarReservaCommand command);
}
