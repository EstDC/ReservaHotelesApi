package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.RegistrarPagoCommand;
import com.reservahoteles.application.dto.RegistrarPagoResponse;

public interface RegistrarPagoUseCase {
    RegistrarPagoResponse ejecutar(RegistrarPagoCommand command);
}