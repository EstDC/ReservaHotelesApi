package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.CrearReservaCommand;
import com.reservahoteles.application.dto.CrearReservaResponse;

public interface CrearReservaUseCase {
    CrearReservaResponse ejecutar(CrearReservaCommand command);
}