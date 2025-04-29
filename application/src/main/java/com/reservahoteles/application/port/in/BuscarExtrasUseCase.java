package com.reservahoteles.application.port.in;

import com.PageResponse;
import com.reservahoteles.application.dto.BuscarExtrasCommand;
import com.reservahoteles.application.dto.ExtraResponse;

public interface BuscarExtrasUseCase {
    PageResponse<ExtraResponse> ejecutar(BuscarExtrasCommand cmd);
}