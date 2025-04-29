package com.reservahoteles.application.port.in;

import com.PageResponse;
import com.reservahoteles.application.dto.BuscarReservasCommand;
import com.reservahoteles.application.dto.ReservaResponse;

public interface BuscarReservasUseCase {
    PageResponse<ReservaResponse> ejecutar(BuscarReservasCommand cmd);
}