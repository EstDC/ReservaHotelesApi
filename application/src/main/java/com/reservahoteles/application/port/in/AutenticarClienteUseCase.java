package com.reservahoteles.application.port.in;


import com.reservahoteles.application.dto.AutenticarClienteCommand;
import com.reservahoteles.application.dto.AutenticarClienteResponse;

public interface AutenticarClienteUseCase {
    AutenticarClienteResponse ejecutar(AutenticarClienteCommand command);
}