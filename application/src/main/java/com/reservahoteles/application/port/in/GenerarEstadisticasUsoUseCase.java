package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.EstadisticasUsoResponse;
import com.reservahoteles.application.dto.GenerarEstadisticasUsoCommand;

public interface GenerarEstadisticasUsoUseCase {
    EstadisticasUsoResponse ejecutar(GenerarEstadisticasUsoCommand command);
}