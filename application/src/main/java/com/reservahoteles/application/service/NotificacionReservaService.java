package com.reservahoteles.application.service;

import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.domain.event.ReservationChangeEvent;
import com.reservahoteles.domain.port.out.NotificationPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificacionReservaService {

    private final NotificationPublisher notifier;

    public NotificacionReservaService(NotificationPublisher notifier) {
        this.notifier = notifier;
    }

    public void notificarCambioReserva(Long idReserva, EstadoReserva estadoReserva) {
        notifier.publishReservationChange(
                new ReservationChangeEvent(
                        idReserva,
                        estadoReserva,
                        LocalDateTime.now()
                )
        );
    }
} 