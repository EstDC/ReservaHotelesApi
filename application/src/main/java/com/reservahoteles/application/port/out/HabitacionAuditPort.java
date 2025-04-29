package com.reservahoteles.application.port.out;
import com.reservahoteles.application.dto.RevisionHabitacionDTO;
import java.util.List;

public interface HabitacionAuditPort {
    List<RevisionHabitacionDTO> findRevisions(Long idHabitacion);
}