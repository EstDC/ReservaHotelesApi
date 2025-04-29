package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.ActualizarClienteCommand;
import com.reservahoteles.application.dto.ActualizarClienteResponse;

public interface ActualizarClienteUseCase {
    ActualizarClienteResponse ejecutar(ActualizarClienteCommand command);
}