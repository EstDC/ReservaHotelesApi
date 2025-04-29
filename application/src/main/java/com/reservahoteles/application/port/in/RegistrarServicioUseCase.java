package com.reservahoteles.application.port.in;


import com.reservahoteles.application.dto.RegistrarServicioCommand;
import com.reservahoteles.application.dto.RegistrarServicioResponse;

public interface RegistrarServicioUseCase {
    RegistrarServicioResponse ejecutar(RegistrarServicioCommand cmd);
}