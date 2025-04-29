package com.reservahoteles.application.service;


import com.reservahoteles.application.dto.CancelarReservaCommand;
import com.reservahoteles.application.dto.CancelarReservaResponse;
import com.reservahoteles.application.port.in.CancelarReservaUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.domain.entity.Reserva;
import com.reservahoteles.domain.event.ReservationChangeEvent;
import com.reservahoteles.domain.port.out.ReservaRepository;
import jakarta.transaction.Transactional;
import com.reservahoteles.domain.port.out.NotificationPublisher;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
public class CancelarReservaService implements CancelarReservaUseCase {

    private final ReservaRepository     reservaRepo;
    private final NotificationPublisher notifier;

    /** Días de antelación mínima para que un cliente (no admin) pueda cancelar */
    private static final long DIAS_LIMITE_CLIENTE = 1;

    public CancelarReservaService(ReservaRepository reservaRepo,
                                  NotificationPublisher notifier) {
        this.reservaRepo = reservaRepo;
        this.notifier   = notifier;
    }

    @Override
    public CancelarReservaResponse ejecutar(CancelarReservaCommand cmd) {
        // 1) Recuperar reserva
        Reserva res = reservaRepo.findById(cmd.idReserva())
                .orElseThrow(() -> new NoSuchElementException(
                        "No existe la reserva con id = " + cmd.idReserva()
                ));

        // 2) Permisos
        boolean esAdmin  = cmd.rolActor() == Rol.ADMINISTRADOR;
        boolean esPropia = Objects.equals(res.getIdCliente(), cmd.idActor());
        if (!esAdmin && !esPropia) {
            return fail(cmd, res.getEstadoReserva(),
                    "No tiene permisos para cancelar esta reserva");
        }

        // 3) Estado actual permite cancelación?
        if (res.getEstadoReserva() == EstadoReserva.CANCELADA) {
            return new CancelarReservaResponse(
                    res.getId(),
                    EstadoReserva.CANCELADA,
                    "La reserva ya está cancelada"
            );
        }
        if (res.getEstadoReserva() == EstadoReserva.FINALIZADA) {
            return fail(cmd, res.getEstadoReserva(),
                    "No se puede cancelar una reserva ya finalizada");
        }

        // 4) Regla de antelación (solo para clientes)
        if (!esAdmin) {
            LocalDate hoy      = LocalDate.now();
            LocalDate fechaEnt = res.getFechaEntrada();
            // requiere al menos un día de antelación
            if (!hoy.isBefore(fechaEnt.minusDays(DIAS_LIMITE_CLIENTE))) {
                return fail(cmd, res.getEstadoReserva(),
                        "No se puede cancelar con menos de " +
                                DIAS_LIMITE_CLIENTE + " día(s) de antelación"
                );
            }
        }

        // 5) Ejecutar cancelación
        res.setEstadoReserva(EstadoReserva.CANCELADA);
        res.setUltimaActualizacion(LocalDateTime.now());
        reservaRepo.save(res);

        // 6) Notificar en tiempo real
        notifier.publishReservationChange(
                new ReservationChangeEvent(
                        res.getId(),
                        EstadoReserva.CANCELADA,
                        LocalDateTime.now()
                )
        );

        return new CancelarReservaResponse(
                res.getId(),
                EstadoReserva.CANCELADA,
                "Reserva cancelada correctamente"
        );
    }

    private CancelarReservaResponse fail(CancelarReservaCommand cmd,
                                         EstadoReserva estadoActual,
                                         String msg) {
        return new CancelarReservaResponse(
                cmd.idReserva(),
                estadoActual,
                msg
        );
    }
}