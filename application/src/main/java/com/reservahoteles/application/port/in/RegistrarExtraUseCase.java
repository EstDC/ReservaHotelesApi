package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.RegistrarExtraCommand;
import com.reservahoteles.application.dto.RegistrarExtraResponse;

public interface RegistrarExtraUseCase {
    RegistrarExtraResponse ejecutar(RegistrarExtraCommand cmd);
}