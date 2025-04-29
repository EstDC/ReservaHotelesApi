package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.SolicitarExtrasReservaCommand;
import com.reservahoteles.application.dto.SolicitarExtrasReservaResponse;

public interface SolicitarExtrasReservaUseCase {
    SolicitarExtrasReservaResponse ejecutar(SolicitarExtrasReservaCommand cmd);
}