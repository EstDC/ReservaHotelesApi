package com.reservahoteles.application.service;
import com.reservahoteles.application.dto.*;
import com.reservahoteles.application.port.in.GenerarReporteIngresosUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.domain.entity.Reserva;
import com.reservahoteles.domain.port.out.HabitacionRepository;
import com.reservahoteles.domain.port.out.HotelRepository;
import com.reservahoteles.domain.port.out.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GenerarReporteIngresosService implements GenerarReporteIngresosUseCase {

    private final ReservaRepository reservaRepo;
    private final HabitacionRepository habitacionRepo;
    private final HotelRepository hotelRepo;

    public GenerarReporteIngresosService(ReservaRepository reservaRepo,
                                         HabitacionRepository habitacionRepo,
                                         HotelRepository hotelRepo) {
        this.reservaRepo    = reservaRepo;
        this.habitacionRepo = habitacionRepo;
        this.hotelRepo      = hotelRepo;
    }

    @Override
    public ReporteIngresosResponse ejecutar(GenerarReporteIngresosCommand cmd) {
        // 1) Permisos: solo ADMIN
        if (cmd.rolActor() != Rol.ADMINISTRADOR) {
            return new ReporteIngresosResponse(
                    cmd.fechaInicio(), cmd.fechaFin(),
                    Collections.emptyList(), 0.0,
                    "FAILURE", "Solo el administrador puede generar este reporte"
            );
        }

        // 2) Validar fechas
        LocalDate inicio = cmd.fechaInicio();
        LocalDate fin    = cmd.fechaFin();
        if (inicio == null || fin == null) {
            return new ReporteIngresosResponse(
                    inicio, fin, Collections.emptyList(), 0.0,
                    "FAILURE", "Debe indicar fechaInicio y fechaFin"
            );
        }
        if (inicio.isAfter(fin)) {
            return new ReporteIngresosResponse(
                    inicio, fin, Collections.emptyList(), 0.0,
                    "FAILURE", "fechaInicio no puede ser posterior a fechaFin"
            );
        }
        // (opcional) limitar rango a, p.ej., 1 año
        if (inicio.plusYears(1).isBefore(fin)) {
            return new ReporteIngresosResponse(
                    inicio, fin, Collections.emptyList(), 0.0,
                    "FAILURE", "El rango no puede exceder 1 año"
            );
        }

        // 3) Si filtra por hotel, verificar que exista
        Long filtroHotel = cmd.idHotel();
        if (filtroHotel != null && hotelRepo.findById(filtroHotel).isEmpty()) {
            return new ReporteIngresosResponse(
                    inicio, fin, Collections.emptyList(), 0.0,
                    "FAILURE", "No existe el hotel id=" + filtroHotel
            );
        }

        // 4) Traer reservas en el rango y estados válidos
        List<Reserva> todas = reservaRepo
                .buscarPorCriteriosAvanzados(
                        null,
                        filtroHotel,
                        inicio, fin,
                        null             // estado null → luego filtramos
                )
                .stream()
                .filter(r -> r.getEstadoReserva() == EstadoReserva.CONFIRMADA
                        || r.getEstadoReserva() == EstadoReserva.FINALIZADA)
                .collect(Collectors.toList());

        // 5) Agrupar ingresos por hotel
        Map<Long, Double> sumaPorHotel = new HashMap<>();
        for (Reserva r : todas) {
            sumaPorHotel.merge(
                    r.getIdHabitacion(),
                    r.getTotal(),
                    Double::sum
            );
        }

        // 6) Traducir habitación → hotel y construir DTOs
        List<IngresoPorHotelDTO> ingresos = sumaPorHotel.entrySet().stream()
                .map(e -> {
                    Long idHabitacion = e.getKey();
                    Double ingreso    = e.getValue();

                    // En lugar de reservaRepo.findByIdHabitacion, usamos habitacionRepo
                    var hotelOpt = habitacionRepo.findById(idHabitacion)
                            .flatMap(hab -> hotelRepo.findById(hab.getIdHotel()));

                    Long   idHotel     = hotelOpt.map(h -> h.getId()).orElse(-1L);
                    String nombreHotel = hotelOpt.map(h -> h.getNombre()).orElse("Desconocido");

                    return new IngresoPorHotelDTO(idHotel, nombreHotel, ingreso);
                })
                .sorted(Comparator.comparing(IngresoPorHotelDTO::ingresos).reversed())
                .toList();

        // 7) Total global
        double total = ingresos.stream()
                .mapToDouble(IngresoPorHotelDTO::ingresos)
                .sum();

        return new ReporteIngresosResponse(
                inicio, fin, ingresos, total,
                "SUCCESS", "Reporte de ingresos generado correctamente"
        );
    }
}

//Solo un ADMINISTRADOR puede invocarlo.
//
//Se valida que fechaInicio ≤ fechaFin, no más de 1 año de rango y que el hotel (si se pasa) exista.
//
//Se usan las reservas confirmadas o finalizadas dentro del rango.
//
//Primero agrupamos por idHabitacion, luego recuperamos el hotel asoci‑ado para armar el DTO IngresoPorHotelDTO.
//
//Se ordena de mayor a menor ingreso y se calcula un total global.