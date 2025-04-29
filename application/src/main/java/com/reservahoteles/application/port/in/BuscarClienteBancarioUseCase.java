package com.reservahoteles.application.port.in;

import com.PageResponse;
import com.reservahoteles.application.dto.BuscarClienteBancarioCommand;
import com.reservahoteles.application.dto.ClienteBancarioResponse;

public interface BuscarClienteBancarioUseCase {
    PageResponse<ClienteBancarioResponse> ejecutar(BuscarClienteBancarioCommand cmd);
}