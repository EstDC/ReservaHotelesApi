package com.reservahoteles.application.port.in;
import com.reservahoteles.application.dto.ConfirmarRecuperarContrasenaCommand;
import com.reservahoteles.application.dto.ConfirmarRecuperarContrasenaResponse;

public interface ConfirmarRecuperarContrasenaUseCase {
    ConfirmarRecuperarContrasenaResponse ejecutar(ConfirmarRecuperarContrasenaCommand command);
}