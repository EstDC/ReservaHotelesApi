package com.reservahoteles.domain.port.out;
import com.reservahoteles.domain.event.ReservationChangeEvent;

/**
 * Publicador de eventos de reserva para notificaciones en tiempo real.
 */

public interface NotificationPublisher {
    void publishReservationChange(ReservationChangeEvent event);
}