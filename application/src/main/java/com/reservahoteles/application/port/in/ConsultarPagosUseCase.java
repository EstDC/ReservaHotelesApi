package com.reservahoteles.application.port.in;


import com.PageResponse;
import com.reservahoteles.application.dto.ConsultarPagosCommand;
import com.reservahoteles.application.dto.PagoResponse;

import java.util.List;
/**
 * Interfaz que define el caso de uso para consultar pagos.
 */
public interface ConsultarPagosUseCase {
    PageResponse<PagoResponse> ejecutar(ConsultarPagosCommand command);
}