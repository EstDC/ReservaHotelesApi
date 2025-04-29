package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.ActualizarHotelCommand;
import com.reservahoteles.application.dto.ActualizarHotelResponse;

public interface ActualizarHotelUseCase {
    ActualizarHotelResponse ejecutar(ActualizarHotelCommand command);
}