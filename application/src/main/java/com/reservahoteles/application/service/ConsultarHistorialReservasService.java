package com.reservahoteles.application.service;
import com.PageResponse;
import com.reservahoteles.application.dto.ConsultarHistorialReservasCommand;
import com.reservahoteles.application.dto.HistorialReservaResponse;
import com.reservahoteles.application.port.in.ConsultarHistorialReservasUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.HistorialReserva;
import com.reservahoteles.domain.port.out.HistorialReservaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ConsultarHistorialReservasService implements ConsultarHistorialReservasUseCase {

    private final HistorialReservaRepository historialRepo;

    @Override
    public PageResponse<HistorialReservaResponse> ejecutar(ConsultarHistorialReservasCommand cmd) {
        // 1) Solo administradores
        if (cmd.rolActor() != Rol.ADMINISTRADOR) {
            throw new IllegalStateException("Solo los administradores pueden consultar el historial completo de reservas");
        }

        // 2) Validar rango de fechas
        if (cmd.fechaInicio() != null && cmd.fechaFin() != null
                && cmd.fechaInicio().isAfter(cmd.fechaFin())) {
            throw new IllegalArgumentException("Rango de fechas inválido: fechaInicio > fechaFin");
        }

        // 3) Crear Pageable (página, tamaño y orden por fechaArchivo descendente)
        Pageable pageable = PageRequest.of(cmd.page(), cmd.size(), Sort.by("fechaArchivo").descending());

        // 4) Recuperar página del repositorio con todos los filtros opcionales
        Page<HistorialReserva> page = historialRepo.buscarPorCriteriosAvanzados(
                cmd.idReserva(),
                cmd.idCliente(),
                cmd.idHabitacion(),
                cmd.fechaInicio(),
                cmd.fechaFin(),
                cmd.estadoReserva(),
                pageable
        );
        List<HistorialReserva> encontrados = page.getContent();

        // 5) Mapear cada entidad de historial a su DTO de respuesta
        List<HistorialReservaResponse> items = encontrados.stream()
                .map(hr -> new HistorialReservaResponse(
                        hr.getId(),
                        hr.getIdReserva(),
                        hr.getIdCliente(),
                        hr.getIdHabitacion(),
                        hr.getFechaEntrada(),
                        hr.getFechaSalida(),
                        hr.getEstadoReserva(),
                        hr.getFechaReserva(),
                        hr.getTotal(),
                        hr.getFechaArchivo()
                ))
                .collect(Collectors.toList());

        // 6) Devolver PageResponse con metadatos de paginación
        return new PageResponse<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}