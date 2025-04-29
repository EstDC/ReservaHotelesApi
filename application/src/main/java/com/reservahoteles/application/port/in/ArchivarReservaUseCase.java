package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.ArchivarReservaCommand;
import com.reservahoteles.application.dto.ArchivarReservaResponse;

public interface ArchivarReservaUseCase {
    ArchivarReservaResponse ejecutar(ArchivarReservaCommand command);
}