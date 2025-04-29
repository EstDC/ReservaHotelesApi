package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.RecuperarContrasenaCommand;
import com.reservahoteles.application.dto.RecuperarContrasenaResponse;

public interface RecuperarContrasenaUseCase {
    RecuperarContrasenaResponse ejecutar(RecuperarContrasenaCommand command);
}