package com.reservahoteles.application.port.in;


import com.PageResponse;
import com.reservahoteles.application.dto.ConsultarHabitacionesCommand;
import com.reservahoteles.application.dto.HabitacionResponse;

import java.util.List;

public interface ConsultarHabitacionesPorHotelUseCase {
    PageResponse<HabitacionResponse> ejecutar(ConsultarHabitacionesCommand command);
}