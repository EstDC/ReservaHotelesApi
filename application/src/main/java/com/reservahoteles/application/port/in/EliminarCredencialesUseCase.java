package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.EliminarCredencialesCommand;
import com.reservahoteles.application.dto.EliminarCredencialesResponse;

public interface EliminarCredencialesUseCase {
    EliminarCredencialesResponse ejecutar(EliminarCredencialesCommand command);
}