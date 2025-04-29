package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.ActualizarEstadoPagoCommand;
import com.reservahoteles.application.dto.ActualizarEstadoPagoResponse;

public interface ActualizarEstadoPagoUseCase {
    ActualizarEstadoPagoResponse ejecutar(ActualizarEstadoPagoCommand command);
}