package com.reservahoteles.application.port.in;


import com.reservahoteles.application.dto.CancelarReservaCommand;
import com.reservahoteles.application.dto.CancelarReservaResponse;

public interface CancelarReservaUseCase {
    CancelarReservaResponse ejecutar(CancelarReservaCommand command);
}