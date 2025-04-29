package com.reservahoteles.application.port.in;

import com.PageResponse;
import com.reservahoteles.application.dto.BuscarClientesCommand;
import com.reservahoteles.application.dto.BuscarClientesResponse;

import java.util.List;


public interface BuscarClientesUseCase {
    PageResponse<BuscarClientesResponse> ejecutar(BuscarClientesCommand command);
}