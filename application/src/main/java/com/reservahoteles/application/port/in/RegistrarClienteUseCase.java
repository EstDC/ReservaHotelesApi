package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.RegistrarClienteCommand;
import com.reservahoteles.application.dto.RegistrarClienteResponse;

public interface RegistrarClienteUseCase {
    RegistrarClienteResponse ejecutar(RegistrarClienteCommand command);
}