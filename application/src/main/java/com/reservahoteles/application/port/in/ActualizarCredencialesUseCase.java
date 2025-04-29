package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.ActualizarCredencialesCommand;
import com.reservahoteles.application.dto.ActualizarCredencialesResponse;

public interface ActualizarCredencialesUseCase {
    ActualizarCredencialesResponse ejecutar(ActualizarCredencialesCommand command);
}