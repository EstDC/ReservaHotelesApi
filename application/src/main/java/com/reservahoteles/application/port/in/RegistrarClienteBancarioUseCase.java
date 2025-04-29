package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.RegistrarClienteBancarioCommand;
import com.reservahoteles.application.dto.RegistrarClienteBancarioResponse;

public interface RegistrarClienteBancarioUseCase {
    RegistrarClienteBancarioResponse ejecutar(RegistrarClienteBancarioCommand command);
}