package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.EliminarClienteCommand;
import com.reservahoteles.application.dto.EliminarClienteResponse;

public interface EliminarClienteUseCase {
    EliminarClienteResponse ejecutar(EliminarClienteCommand command);
}