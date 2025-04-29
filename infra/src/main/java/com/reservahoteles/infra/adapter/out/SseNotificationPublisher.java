package com.reservahoteles.infra.adapter.out;

import com.reservahoteles.domain.event.ReservationChangeEvent;
import com.reservahoteles.domain.port.out.NotificationPublisher;
import com.reservahoteles.infra.sse.SseEmitterService;
import org.springframework.stereotype.Component;

@Component
public class SseNotificationPublisher implements NotificationPublisher {

    private final SseEmitterService emitterService;

    public SseNotificationPublisher(SseEmitterService emitterService) {
        this.emitterService = emitterService;
    }

    @Override
    public void publishReservationChange(ReservationChangeEvent event) {
        emitterService.sendEvent(event);
    }
}