package com.reservahoteles.application.port.in;


import com.PageResponse;
import com.reservahoteles.application.dto.ConsultarHistorialHabitacionCommand;
import com.reservahoteles.application.dto.RevisionHabitacionDTO;

import java.util.List;

public interface ConsultarHistorialHabitacionUseCase {
    PageResponse<RevisionHabitacionDTO> ejecutar(ConsultarHistorialHabitacionCommand command);
}