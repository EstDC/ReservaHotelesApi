package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.AsignarExtrasHabitacionCommand;
import com.reservahoteles.application.dto.AsignarExtrasHabitacionResponse;

public interface AsignarExtrasHabitacionUseCase {
    AsignarExtrasHabitacionResponse ejecutar(AsignarExtrasHabitacionCommand command);
}