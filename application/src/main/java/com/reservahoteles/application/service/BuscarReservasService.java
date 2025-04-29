package com.reservahoteles.application.service;
import com.PageResponse;
import com.reservahoteles.application.dto.BuscarReservasCommand;
import com.reservahoteles.application.dto.ReservaResponse;
import com.reservahoteles.application.port.in.BuscarReservasUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.common.enums.EstadoPago;
import com.reservahoteles.domain.entity.Extra;
import com.reservahoteles.domain.entity.Habitacion;
import com.reservahoteles.domain.entity.Reserva;
import com.reservahoteles.domain.port.out.HabitacionRepository;
import com.reservahoteles.domain.port.out.PagoRepository;
import com.reservahoteles.domain.port.out.ReservaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BuscarReservasService implements BuscarReservasUseCase {

    private final ReservaRepository    reservaRepo;
    private final HabitacionRepository habitacionRepo;
    private final PagoRepository       pagoRepo;

    public BuscarReservasService(ReservaRepository reservaRepo,
                                 HabitacionRepository habitacionRepo,
                                 PagoRepository pagoRepo) {
        this.reservaRepo    = reservaRepo;
        this.habitacionRepo = habitacionRepo;
        this.pagoRepo       = pagoRepo;
    }

    @Override
    public PageResponse<ReservaResponse> ejecutar(BuscarReservasCommand cmd) {
        // 1) idActor + rol obligatorios
        if (cmd.idActor() == null || cmd.rolActor() == null) {
            throw new IllegalArgumentException("Debe suministrar idActor y rolActor");
        }

        // 2) Si piden un ID concreto, lo devolvemos en un "page" de 1
        if (cmd.idReserva() != null) {
            Reserva r = reservaRepo.findById(cmd.idReserva())
                    .orElseThrow(() -> new NoSuchElementException(
                            "No existe reserva con id = " + cmd.idReserva()
                    ));
            // permisos: ADMIN o propietario
            boolean esAdmin  = cmd.rolActor() == Rol.ADMINISTRADOR;
            boolean esPropio = Objects.equals(r.getIdCliente(), cmd.idActor());
            if (!esAdmin && !esPropio) {
                throw new IllegalStateException("No tiene permiso para consultar esta reserva");
            }
            var dto = toDto(r);
            // devolvemos un PageResponse "simulado"
            return new PageResponse<>(
                    List.of(dto),
                    0,  // page
                    1,  // size
                    1,  // totalElements
                    1   // totalPages
            );
        }

        // 3) Construir Pageable (página, tamaño y orden)
        Pageable pageable = PageRequest.of(
                cmd.page(),
                cmd.size(),
                Sort.by("fechaReserva").descending()
        );

        // 4) Llamada paginada a repositorio (filtros básicos)
        LocalDate desde = cmd.fechaInicio();
        LocalDate hasta = cmd.fechaFin();
        Page<Reserva> page = reservaRepo.buscarPorCriteriosAvanzados(
                cmd.idCliente(),
                cmd.idHabitacion(),
                desde,
                hasta,
                cmd.estadoReserva(),
                pageable
        );

        // 5) Filtrados adicionales en memoria
        Stream<Reserva> flujo = aplicarFiltrosExtra(cmd, page);

        // 6) Mapear contenido a DTOs
        List<ReservaResponse> items = flujo
                .map(this::toDto)
                .collect(Collectors.toList());

        // 7) Devolver PageResponse con metadatos reales
        return new PageResponse<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    private ReservaResponse toDto(Reserva r) {
        List<Long> extras = r.getExtras()==null
                ? List.of()
                : r.getExtras().stream().map(Extra::getId).toList();
        return new ReservaResponse(
                r.getId(),
                r.getIdCliente(),
                r.getIdHabitacion(),
                r.getFechaEntrada(),
                r.getFechaSalida(),
                r.getEstadoReserva(),
                r.getFechaReserva(),
                r.getTotal(),
                r.getUltimaActualizacion(),
                extras
        );
    }

    private Stream<Reserva> aplicarFiltrosExtra(BuscarReservasCommand cmd, Page<Reserva> page) {
        Stream<Reserva> flujo = page.getContent().stream();

        // filtro por hotel / estadoHabitacion / tipoHabitacion
        if (cmd.idHotel()!=null || cmd.estadoHabitacion()!=null || cmd.tipoHabitacion()!=null) {
            flujo = flujo.filter(r -> {
                Habitacion h = habitacionRepo.findById(r.getIdHabitacion())
                        .orElse(null);
                if (h==null) return false;
                if (cmd.idHotel()!=null && !Objects.equals(h.getIdHotel(), cmd.idHotel()))
                    return false;
                if (cmd.estadoHabitacion()!=null && h.getEstado()!=cmd.estadoHabitacion())
                    return false;
                if (cmd.tipoHabitacion()!=null && h.getTipo()!=cmd.tipoHabitacion())
                    return false;
                return true;
            });
        }

        // filtro por estadoPago
        if (cmd.estadoPago()!=null) {
            flujo = flujo.filter(r ->
                    !pagoRepo.buscarPorCriterios(
                            r.getId(),
                            null, null, null,
                            cmd.estadoPago()
                    ).isEmpty()
            );
        }

        return flujo;
    }
}