package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.RegistrarHabitacionCommand;
import com.reservahoteles.application.dto.RegistrarHabitacionResponse;

public interface RegistrarHabitacionUseCase {
    RegistrarHabitacionResponse ejecutar(RegistrarHabitacionCommand command);
}