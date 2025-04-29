package com.reservahoteles.application.service;
import com.reservahoteles.application.dto.*;
import com.reservahoteles.application.port.in.GenerarReporteDesempenoUseCase;
import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.*;
import com.reservahoteles.domain.port.out.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GenerarReporteDesempenoService implements GenerarReporteDesempenoUseCase {

    private final HotelRepository      hotelRepo;
    private final HabitacionRepository habitacionRepo;
    private final ReservaRepository    reservaRepo;

    public GenerarReporteDesempenoService(HotelRepository hotelRepo,
                                          HabitacionRepository habitacionRepo,
                                          ReservaRepository reservaRepo) {
        this.hotelRepo       = hotelRepo;
        this.habitacionRepo  = habitacionRepo;
        this.reservaRepo     = reservaRepo;
    }

    @Override
    public ReporteDesempenoResponse ejecutar(GenerarReporteDesempenoCommand cmd) {
        // a) Solo ADMIN
        if (cmd.rolActor() != Rol.ADMINISTRADOR) {
            return new ReporteDesempenoResponse(
                    cmd.fechaInicio(), cmd.fechaFin(),
                    Collections.emptyList(),
                    "FAILURE", "Permisos insuficientes"
            );
        }
        // b) Fechas obligatorias y rango válido
        if (cmd.fechaInicio() == null || cmd.fechaFin() == null) {
            throw new IllegalArgumentException("Debe indicar fechaInicio y fechaFin");
        }
        if (cmd.fechaInicio().isAfter(cmd.fechaFin())) {
            throw new IllegalArgumentException("fechaInicio debe ser anterior o igual a fechaFin");
        }

        // c) Determinar hoteles a procesar
        List<Hotel> hoteles = cmd.idHotel() != null
                ? hotelRepo.findById(cmd.idHotel())
                .map(List::of)
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró hotel id=" + cmd.idHotel()))
                : hotelRepo.findAll();

        long totalDias = ChronoUnit.DAYS.between(cmd.fechaInicio(), cmd.fechaFin()) + 1;

        var datos = hoteles.stream()
                .map(hotel -> {
                    // todas las habitaciones del hotel (paginado unpaged)
                    List<Habitacion> habs = habitacionRepo.buscarPorHotel(
                            hotel.getId(),
                            null,
                            null,
                            Pageable.unpaged()
                    ).getContent();
                    List<Long> habIds = habs.stream().map(Habitacion::getId).toList();

                    // todas las reservas en el rango [fechaInicio, fechaFin] (paginado unpaged)
                    List<Reserva> todas = habIds.stream()
                            .flatMap(hid ->
                                    reservaRepo.buscarPorCriteriosAvanzados(
                                            null, hid, cmd.fechaInicio(), cmd.fechaFin(), null,
                                            Pageable.unpaged()
                                    ).getContent().stream()
                            ).collect(Collectors.toList());

                    long totalRes   = todas.size();
                    long conf       = todas.stream()
                            .filter(r -> r.getEstadoReserva() != EstadoReserva.CANCELADA
                                    && r.getEstadoReserva() != EstadoReserva.FINALIZADA)
                            .count();
                    long cancel     = todas.stream()
                            .filter(r -> r.getEstadoReserva() == EstadoReserva.CANCELADA)
                            .count();
                    double ingreso  = todas.stream().mapToDouble(Reserva::getTotal).sum();
                    double durProm  = todas.stream()
                            .mapToLong(r -> ChronoUnit.DAYS.between(r.getFechaEntrada(), r.getFechaSalida()))
                            .average()
                            .orElse(0.0);
                    long nochesTot  = todas.stream()
                            .filter(r -> r.getEstadoReserva() != EstadoReserva.CANCELADA
                                    && r.getEstadoReserva() != EstadoReserva.FINALIZADA)
                            .mapToLong(r -> ChronoUnit.DAYS.between(r.getFechaEntrada(), r.getFechaSalida()))
                            .sum();
                    double tasaOcup = habs.isEmpty() ? 0.0
                            : (100.0 * nochesTot) / (habs.size() * totalDias);

                    return new DesempenoHotelDTO(
                            hotel.getId(),
                            hotel.getNombre(),
                            totalRes,
                            conf,
                            cancel,
                            ingreso,
                            durProm,
                            tasaOcup
                    );
                })
                .collect(Collectors.toList());

        return new ReporteDesempenoResponse(
                cmd.fechaInicio(),
                cmd.fechaFin(),
                datos,
                "SUCCESS",
                "Reporte de desempeño generado correctamente"
        );
    }
}