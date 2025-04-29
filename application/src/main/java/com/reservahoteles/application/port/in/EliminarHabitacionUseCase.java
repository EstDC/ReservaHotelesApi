package com.reservahoteles.application.port.in;

import com.reservahoteles.application.dto.EliminarHabitacionCommand;
import com.reservahoteles.application.dto.EliminarHabitacionResponse;

public interface EliminarHabitacionUseCase {
    EliminarHabitacionResponse ejecutar(EliminarHabitacionCommand command);
}