package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.AsignarServiciosHotelCommand;
import com.reservahoteles.application.dto.AsignarServiciosHotelResponse;

public interface AsignarServiciosHotelUseCase {
    AsignarServiciosHotelResponse ejecutar(AsignarServiciosHotelCommand command);
}