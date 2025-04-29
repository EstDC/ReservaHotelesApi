package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.EliminarClienteBancarioCommand;
import com.reservahoteles.application.dto.EliminarClienteBancarioResponse;

public interface EliminarClienteBancarioUseCase {
    EliminarClienteBancarioResponse ejecutar(EliminarClienteBancarioCommand command);
}
