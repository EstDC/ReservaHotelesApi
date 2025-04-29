package com.reservahoteles.infra.sse;

import com.reservahoteles.domain.event.ReservationChangeEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class SseEmitterService {
    private final Set<SseEmitter> emitters = new CopyOnWriteArraySet<>();

    /**
     * Crea y devuelve un nuevo SseEmitter, y lo registra para futuras notificaciones.
     */
    public SseEmitter createEmitter() {
        SseEmitter emitter = new SseEmitter(0L); // sin timeout
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout  (() -> emitters.remove(emitter));
        return emitter;
    }

    /**
     * Envía el evento a todos los clientes conectados.
     */
    public void sendEvent(ReservationChangeEvent event) {
        for (SseEmitter emitter : Collections.unmodifiableSet(emitters)) {
            try {
                emitter.send(event);
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }
}