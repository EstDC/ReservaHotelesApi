package com.reservahoteles.application.port.in;


import com.reservahoteles.application.dto.GenerarReporteDesempenoCommand;
import com.reservahoteles.application.dto.ReporteDesempenoResponse;

import java.util.List;

public interface GenerarReporteDesempenoUseCase {
    /**
     * Genera un reporte de desempeño entre dos fechas
     * (y opcionalmente para un hotel concreto).
     */
    ReporteDesempenoResponse ejecutar(GenerarReporteDesempenoCommand command);
}