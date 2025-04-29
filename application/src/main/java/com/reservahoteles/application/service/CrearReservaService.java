package com.reservahoteles.application.service;
import com.reservahoteles.application.dto.CrearReservaCommand;
import com.reservahoteles.application.dto.CrearReservaResponse;
import com.reservahoteles.application.port.in.CrearReservaUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.domain.entity.Extra;
import com.reservahoteles.domain.entity.Reserva;
import com.reservahoteles.domain.port.out.ClienteRepository;
import com.reservahoteles.domain.port.out.ExtraRepository;
import com.reservahoteles.domain.port.out.HabitacionRepository;
import com.reservahoteles.domain.port.out.ReservaRepository;
import com.reservahoteles.domain.port.out.NotificationPublisher;
import com.reservahoteles.domain.event.ReservationChangeEvent;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CrearReservaService implements CrearReservaUseCase {

    private final ReservaRepository    reservaRepo;
    private final ClienteRepository    clienteRepo;
    private final HabitacionRepository habitacionRepo;
    private final ExtraRepository      extraRepo;
    private final NotificationPublisher notifier;

    public CrearReservaService(ReservaRepository reservaRepo,
                               ClienteRepository clienteRepo,
                               HabitacionRepository habitacionRepo,
                               ExtraRepository extraRepo,
                               NotificationPublisher notifier) {
        this.reservaRepo    = reservaRepo;
        this.clienteRepo    = clienteRepo;
        this.habitacionRepo = habitacionRepo;
        this.extraRepo      = extraRepo;
        this.notifier       = notifier;
    }

    @Override
    public CrearReservaResponse ejecutar(CrearReservaCommand cmd) {
        // 1) Solo clientes pueden reservar para sí mismos
        if (cmd.rolActor() != Rol.CLIENTE || !cmd.idActor().equals(cmd.idCliente())) {
            return new CrearReservaResponse(null, "FAILURE",
                    "Solo un cliente logueado puede crear su propia reserva");
        }

        // 2) Validar que cliente existe
        clienteRepo.findById(cmd.idCliente())
                .orElseThrow(() -> new NoSuchElementException(
                        "Cliente no encontrado (id=" + cmd.idCliente() + ")"
                ));

        // 3) Validar fechas
        LocalDate hoy = LocalDate.now();
        if (cmd.fechaEntrada() == null || cmd.fechaSalida() == null) {
            return new CrearReservaResponse(null, "FAILURE",
                    "Debe indicar fecha de entrada y de salida");
        }
        if (!cmd.fechaEntrada().isAfter(hoy)) {
            return new CrearReservaResponse(null, "FAILURE",
                    "La fecha de entrada debe ser posterior a hoy");
        }
        if (!cmd.fechaEntrada().isBefore(cmd.fechaSalida())) {
            return new CrearReservaResponse(null, "FAILURE",
                    "La fecha de entrada debe ser anterior a la de salida");
        }

        // 4) Validar habitación y disponibilidad
        habitacionRepo.findById(cmd.idHabitacion())
                .orElseThrow(() -> new NoSuchElementException(
                        "Habitación no encontrada (id=" + cmd.idHabitacion() + ")"
                ));
        boolean solapa = reservaRepo.buscarPorCriteriosAvanzados(
                        null,
                        cmd.idHabitacion(),
                        cmd.fechaEntrada(),
                        cmd.fechaSalida(),
                        null
                ).stream()
                .anyMatch(r -> true);
        if (solapa) {
            return new CrearReservaResponse(null, "FAILURE",
                    "Ya existe una reserva que solapa estas fechas");
        }

        // 5) Validar extras si los hay
        List<Extra> extras = List.of();
        if (cmd.idsExtras() != null && !cmd.idsExtras().isEmpty()) {
            extras = extraRepo.findByIds(cmd.idsExtras());
            if (extras.size() != cmd.idsExtras().size()) {
                return new CrearReservaResponse(null, "FAILURE",
                        "Alguno de los extras indicados no existe");
            }
        }

        // 6) Calcular total: noches * precio + suma de extras
        long noches = ChronoUnit.DAYS.between(cmd.fechaEntrada(), cmd.fechaSalida());
        BigDecimal precioNoche = habitacionRepo.findById(cmd.idHabitacion()).get().getPrecioPorNoche();
        BigDecimal totalExtras = extras.stream()
                .map(Extra::getCostoAdicional)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal total = precioNoche.multiply(BigDecimal.valueOf(noches)).add(totalExtras);

        // 7) Construir entidad de dominio
        Reserva nueva = Reserva.builder()
                .idCliente(cmd.idCliente())
                .idHabitacion(cmd.idHabitacion())
                .fechaEntrada(cmd.fechaEntrada())
                .fechaSalida(cmd.fechaSalida())
                .estadoReserva(EstadoReserva.PENDIENTE)
                .fechaReserva(LocalDateTime.now())
                .total(total.doubleValue())
                .extras(extras)
                .build();

        // 8) Persistir
        Reserva guardada = reservaRepo.save(nueva);

        // 9) Notificar cambio (opcional)
        notifier.publishReservationChange(
                new ReservationChangeEvent(
                        guardada.getId(),
                        guardada.getEstadoReserva(),
                        LocalDateTime.now()
                )
        );

        // 10) Responder
        return new CrearReservaResponse(
                guardada.getId(),
                "SUCCESS",
                "Reserva creada correctamente (id=" + guardada.getId() + ")"
        );
    }
}