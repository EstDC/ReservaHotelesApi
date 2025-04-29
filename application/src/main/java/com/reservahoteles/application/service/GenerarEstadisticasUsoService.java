package com.reservahoteles.application.service;

import com.reservahoteles.application.dto.*;
import com.reservahoteles.application.port.in.GenerarEstadisticasUsoUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Reserva;
import com.reservahoteles.domain.port.out.ClienteRepository;
import com.reservahoteles.domain.port.out.HabitacionRepository;
import com.reservahoteles.domain.port.out.HotelRepository;
import com.reservahoteles.domain.port.out.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class GenerarEstadisticasUsoService implements GenerarEstadisticasUsoUseCase {

    private final ReservaRepository reservaRepo;
    private final HotelRepository hotelRepo;
    private final ClienteRepository clienteRepo;
    private final HabitacionRepository habitacionRepo;

    public GenerarEstadisticasUsoService(ReservaRepository reservaRepo,
                                         HotelRepository hotelRepo,
                                         ClienteRepository clienteRepo,
                                         HabitacionRepository habitacionRepo) {
        this.reservaRepo  = reservaRepo;
        this.hotelRepo    = hotelRepo;
        this.clienteRepo  = clienteRepo;
        this.habitacionRepo = habitacionRepo;
    }

    @Override
    public EstadisticasUsoResponse ejecutar(GenerarEstadisticasUsoCommand cmd) {
        // 1) Sólo administradores
        if (cmd.rolActor() != Rol.ADMINISTRADOR) {
            return new EstadisticasUsoResponse(
                    0,0,0,0,
                    Collections.emptyList(),
                    Collections.emptyList(),
                    "FAILURE",
                    "Solo el administrador puede generar estadísticas de uso"
            );
        }

        // 2) Validar fechas
        if (cmd.fechaInicio() == null || cmd.fechaFin() == null) {
            throw new IllegalArgumentException("Debe indicar fechaInicio y fechaFin");
        }
        if (cmd.fechaInicio().isAfter(cmd.fechaFin())) {
            throw new IllegalArgumentException("fechaInicio no puede ser posterior a fechaFin");
        }

        // 3) Obtener reservas en rango (no filtramos estado para incluir todas)
        List<Reserva> todas = reservaRepo.buscarPorCriteriosAvanzados(
                null,              // idCliente
                null,              // idHabitacion
                cmd.fechaInicio(),
                cmd.fechaFin(),
                null               // estadoReserva
        );

        // 4) Métricas básicas
        long totalReservas = todas.size();
        long clientesUnicos = todas.stream()
                .map(Reserva::getIdCliente)
                .distinct()
                .count();
        double ingresos = todas.stream()
                .mapToDouble(Reserva::getTotal)
                .sum();
        double duracionMedia = todas.stream()
                .mapToLong(r -> ChronoUnit.DAYS.between(r.getFechaEntrada(), r.getFechaSalida()))
                .average()
                .orElse(0);

        // 5) Top 5 hoteles por nº de reservas
        Map<Long, Long> countPorHotel = todas.stream()
                .collect(Collectors.groupingBy(Reserva::getIdHabitacion, Collectors.counting()));
        // convertimos habitación→hotel
        Map<Long, Long> reservasPorHotel = new HashMap<>();
        countPorHotel.forEach((idHabitacion, cantidad) ->
                        habitacionRepo.findById(idHabitacion)
                                .ifPresent(habitacion ->
                                        reservasPorHotel.merge(
                                                habitacion.getIdHotel(),
                                                cantidad,
                                                Long::sum
                                        )
                                )
                );
        List<HotelEstadisticaDTO> topHoteles = reservasPorHotel.entrySet().stream()
                .map(en -> {
                    var h = hotelRepo.findById(en.getKey())
                            .orElseThrow();
                    return new HotelEstadisticaDTO(
                            h.getId(),
                            h.getNombre(),
                            en.getValue()
                    );
                })
                .sorted(Comparator.comparingLong(HotelEstadisticaDTO::reservas).reversed())
                .limit(5)
                .toList();

        // 6) Top 5 clientes por nº de reservas
        Map<Long, Long> countPorCliente = todas.stream()
                .collect(Collectors.groupingBy(Reserva::getIdCliente, Collectors.counting()));
        List<ClienteEstadisticaDTO> topClientes = countPorCliente.entrySet().stream()
                .map(en -> {
                    var c = clienteRepo.findById(en.getKey())
                            .orElseThrow();
                    return new ClienteEstadisticaDTO(
                            c.getId(),
                            c.getNombre(),
                            en.getValue()
                    );
                })
                .sorted(Comparator.comparingLong(ClienteEstadisticaDTO::reservas).reversed())
                .limit(5)
                .toList();

        // 7) Devolver
        return new EstadisticasUsoResponse(
                totalReservas,
                clientesUnicos,
                ingresos,
                duracionMedia,
                topHoteles,
                topClientes,
                "SUCCESS",
                "Estadísticas de uso generadas correctamente"
        );
    }
}

//Se comprueba que el actor sea administrador.
//
//Se exige rango de fechas válido (no nulo y inicio ≤ fin).
//
//Se agregan métricas de negocio fundamentales.
//
//Para construir el “top hoteles”, partimos de la relación reserva → habitación → hotel; asumimos un método reservaRepo.findByIdHabitacion(Long) que devuelva la entidad Habitacion. Si no está, habría que inyectar también HabitacionRepository.
//
//Se limitan ambos listados a los 5 primeros.
//
//Si quieres otras métricas (ocupación diaria, ingresos por hotel, reservas por mes…), basta con añadir nuevos cálculos en este mismo flujo.