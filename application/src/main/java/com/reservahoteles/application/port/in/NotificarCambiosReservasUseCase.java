package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.NotificarCambiosReservasCommand;
import com.reservahoteles.application.dto.NotificarCambiosReservasResponse;

public interface NotificarCambiosReservasUseCase {
    NotificarCambiosReservasResponse ejecutar(NotificarCambiosReservasCommand command);
}