package com.reservahoteles.application.port.in;


import com.reservahoteles.application.dto.ActualizarHabitacionCommand;
import com.reservahoteles.application.dto.ActualizarHabitacionResponse;

public interface ActualizarHabitacionUseCase {
    ActualizarHabitacionResponse ejecutar(ActualizarHabitacionCommand command);
}