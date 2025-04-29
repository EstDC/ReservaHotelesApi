package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.EliminarHotelCommand;
import com.reservahoteles.application.dto.EliminarHotelResponse;

public interface EliminarHotelUseCase {
    EliminarHotelResponse ejecutar(EliminarHotelCommand command);
}