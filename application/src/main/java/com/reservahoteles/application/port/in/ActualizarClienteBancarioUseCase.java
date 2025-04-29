package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.ActualizarClienteBancarioCommand;
import com.reservahoteles.application.dto.ActualizarClienteBancarioResponse;

public interface ActualizarClienteBancarioUseCase {
    ActualizarClienteBancarioResponse ejecutar(ActualizarClienteBancarioCommand command);
}