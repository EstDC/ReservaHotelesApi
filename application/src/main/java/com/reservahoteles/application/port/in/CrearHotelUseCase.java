package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.CrearHotelCommand;
import com.reservahoteles.application.dto.CrearHotelResponse;

public interface CrearHotelUseCase {
    CrearHotelResponse ejecutar(CrearHotelCommand command);
}