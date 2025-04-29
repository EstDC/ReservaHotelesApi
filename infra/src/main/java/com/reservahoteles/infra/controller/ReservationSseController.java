package com.reservahoteles.infra.controller;

import com.reservahoteles.infra.sse.SseEmitterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/reservas")
public class ReservationSseController {

    private final SseEmitterService emitterService;

    public ReservationSseController(SseEmitterService emitterService) {
        this.emitterService = emitterService;
    }

    /**
     * Endpoint para que el cliente se suscriba a notificaciones SSE.
     */
    @GetMapping("/stream")
    public SseEmitter streamReservations() {
        return emitterService.createEmitter();
    }
}