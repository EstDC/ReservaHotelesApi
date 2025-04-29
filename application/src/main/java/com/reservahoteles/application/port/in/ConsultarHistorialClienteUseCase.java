package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.ConsultarHistorialClienteCommand;
import com.reservahoteles.application.dto.ConsultarHistorialClienteResponse;

public interface ConsultarHistorialClienteUseCase {
    ConsultarHistorialClienteResponse ejecutar(ConsultarHistorialClienteCommand command);
}