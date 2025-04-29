package com.reservahoteles.application.port.in;
import com.reservahoteles.application.dto.EliminarServicioCommand;
import com.reservahoteles.application.dto.EliminarServicioResponse;

public interface EliminarServicioUseCase {
    EliminarServicioResponse ejecutar(EliminarServicioCommand cmd);
}