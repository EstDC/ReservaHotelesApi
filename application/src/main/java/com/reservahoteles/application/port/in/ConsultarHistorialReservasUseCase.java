package com.reservahoteles.application.port.in;

import com.PageResponse;
import com.reservahoteles.application.dto.ConsultarHistorialReservasCommand;
import com.reservahoteles.application.dto.HistorialReservaResponse;

import java.util.List;

public interface ConsultarHistorialReservasUseCase {
    PageResponse<HistorialReservaResponse> ejecutar(ConsultarHistorialReservasCommand command);
}