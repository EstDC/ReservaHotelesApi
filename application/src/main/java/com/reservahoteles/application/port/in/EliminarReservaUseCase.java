package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.EliminarReservaCommand;
import com.reservahoteles.application.dto.EliminarReservaResponse;

public interface EliminarReservaUseCase {
    EliminarReservaResponse ejecutar(EliminarReservaCommand command);
}