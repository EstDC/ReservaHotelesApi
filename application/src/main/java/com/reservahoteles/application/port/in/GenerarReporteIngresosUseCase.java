package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.GenerarReporteIngresosCommand;
import com.reservahoteles.application.dto.ReporteIngresosResponse;

import java.util.List;

public interface GenerarReporteIngresosUseCase {
    ReporteIngresosResponse ejecutar(GenerarReporteIngresosCommand command);
}