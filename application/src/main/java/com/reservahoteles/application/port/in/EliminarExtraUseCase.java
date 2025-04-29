package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.EliminarExtraCommand;
import com.reservahoteles.application.dto.EliminarExtraResponse;

public interface EliminarExtraUseCase {
    EliminarExtraResponse ejecutar(EliminarExtraCommand cmd);
}