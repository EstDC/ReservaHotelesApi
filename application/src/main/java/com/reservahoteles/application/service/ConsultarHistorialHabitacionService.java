package com.reservahoteles.application.service;

import com.PageResponse;
import com.reservahoteles.application.dto.RevisionHabitacionDTO;
import com.reservahoteles.application.port.in.ConsultarHistorialHabitacionUseCase;
import com.reservahoteles.application.port.out.HabitacionAuditPort;
import org.springframework.stereotype.Service;
import java.util.List;
import com.reservahoteles.application.dto.ConsultarHistorialHabitacionCommand;


@Service
public class ConsultarHistorialHabitacionService implements ConsultarHistorialHabitacionUseCase {

    private final HabitacionAuditPort auditPort;

    public ConsultarHistorialHabitacionService(HabitacionAuditPort auditPort) {
        this.auditPort = auditPort;
    }

    @Override
    public PageResponse<RevisionHabitacionDTO> ejecutar(ConsultarHistorialHabitacionCommand cmd) {
        // 1) Obtener todo el historial
        List<RevisionHabitacionDTO> allRevisions = auditPort.findRevisions(cmd.idHabitacion());

        // 2) Calcular índices de paginación
        int page = cmd.page();
        int size = cmd.size();
        int total = allRevisions.size();
        int fromIndex = page * size;
        int toIndex   = Math.min(fromIndex + size, total);

        List<RevisionHabitacionDTO> items;
        if (fromIndex >= total) {
            items = List.of();
        } else {
            items = allRevisions.subList(fromIndex, toIndex);
        }

        // 3) Construir y devolver el PageResponse
        int totalPages = (int) Math.ceil((double) total / size);
        return new PageResponse<>(
                items,
                page,
                size,
                total,
                totalPages
        );
    }
}