package com.reservahoteles.application.port.in;


import com.PageResponse;
import com.reservahoteles.application.dto.BuscarHotelesCommand;
import com.reservahoteles.application.dto.ListarHotelesResponse;

public interface BuscarHotelesUseCase {
    PageResponse<ListarHotelesResponse> ejecutar(BuscarHotelesCommand cmd);
}