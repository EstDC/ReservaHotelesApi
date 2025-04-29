package com.reservahoteles.application.port.in;

import com.PageResponse;
import com.reservahoteles.application.dto.BuscarServiciosCommand;
import com.reservahoteles.application.dto.ServicioResponse;

public interface BuscarServiciosUseCase {
    PageResponse<ServicioResponse> ejecutar(BuscarServiciosCommand cmd);
}