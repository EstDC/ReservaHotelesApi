package com.reservahoteles.application.service;

import com.reservahoteles.application.dto.SolicitarExtrasReservaCommand;
import com.reservahoteles.application.dto.SolicitarExtrasReservaResponse;
import com.reservahoteles.application.port.in.SolicitarExtrasReservaUseCase;
import com.reservahoteles.common.enums.ModoAsignacion;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Extra;
import com.reservahoteles.domain.entity.Reserva;
import com.reservahoteles.domain.event.ReservationChangeEvent;
import com.reservahoteles.domain.port.out.ExtraRepository;
import com.reservahoteles.domain.port.out.HabitacionRepository;
import com.reservahoteles.domain.port.out.NotificationPublisher;
import com.reservahoteles.domain.port.out.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SolicitarExtrasReservaService implements SolicitarExtrasReservaUseCase {

    private final ReservaRepository     reservaRepo;
    private final ExtraRepository       extraRepo;
    private final NotificationPublisher notifier;
    private final HabitacionRepository habitacionRepo;

    /** Horas mínimas antes del check‑in para que un cliente pueda modificar extras */
    private static final long HORAS_LIMITE = 12;

    @Override
    public SolicitarExtrasReservaResponse ejecutar(SolicitarExtrasReservaCommand cmd) {

        /* 1 ── Cargar reserva ─────────────────────────────────────────── */
        Reserva res = reservaRepo.findById(cmd.idReserva())
                .orElseThrow(() -> new NoSuchElementException(
                        "Reserva no encontrada (id=" + cmd.idReserva() + ')'));

        List<Extra> listaActual = res.getExtras() == null
                ? new ArrayList<>()
                : new ArrayList<>(res.getExtras());
        res.setExtras(listaActual);

        /* 2 ── Seguridad ──────────────────────────────────────────────── */
        boolean esAdmin  = cmd.rolActor() == Rol.ADMINISTRADOR;
        boolean esPropia = Objects.equals(res.getIdCliente(), cmd.idActor());
        if (!esAdmin && !esPropia)
            return fail(cmd, "No tiene permiso para modificar esta reserva");

        /* 3 ── Regla de tiempo (solo cliente) ─────────────────────────── */
        if (!esAdmin) {
            LocalDateTime limite =
                    res.getFechaEntrada()
                            .atTime(14, 0)            // hora de check‑in hipotética
                            .minusHours(HORAS_LIMITE);
            if (LocalDateTime.now().isAfter(limite))
                return fail(cmd,
                        "No se pueden añadir/quitar extras con menos de "
                                + HORAS_LIMITE + " h de antelación");
        }

        /* 4 ── Validar existencia de los extras ───────────────────────── */
        List<Extra> extrasSolicitados = extraRepo.findByIds(cmd.idsExtras());
        if (extrasSolicitados.size() != cmd.idsExtras().size())
            return fail(cmd, "Alguno de los extras solicitados no existe");

        /* 5 ── Aplicar la operación (REEMPLAZAR / AGREGAR / QUITAR) ──── */
        ModoAsignacion modo = cmd.modo() != null ? cmd.modo() : ModoAsignacion.REEMPLAZAR;

        switch (modo) {
            case REEMPLAZAR -> listaActual = extrasSolicitados;

            case AGREGAR -> {
                for (Extra e : extrasSolicitados) {
                    if (!listaActual.contains(e)) {
                        listaActual.add(e);
                    }
                }
            }

            case QUITAR -> listaActual.removeIf(extrasSolicitados::contains);
        }
        res.setExtras(listaActual);

        /* 6 ── Recalcular total */
        BigDecimal precioBase = habitacionRepo.findById(res.getIdHabitacion())
                .orElseThrow(() ->
                        new IllegalStateException("Habitación no encontrada"))
                .getPrecioPorNoche();

        long noches = java.time.temporal.ChronoUnit.DAYS
                .between(res.getFechaEntrada(), res.getFechaSalida());

        noches = Math.max(1, noches);

        BigDecimal totalExtras = res.getExtras().stream()
                .map(Extra::getCostoAdicional)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal total = precioBase.multiply(BigDecimal.valueOf(noches)).add(totalExtras);
        res.setTotal(total.doubleValue());

        /* 7 ── Persistir y notificar ─────────────────────────────────── */
        res.setUltimaActualizacion(LocalDateTime.now());
        reservaRepo.save(res);

        notifier.publishReservationChange(
                new ReservationChangeEvent(res.getId(),
                        res.getEstadoReserva(),
                        LocalDateTime.now())
        );

        return new SolicitarExtrasReservaResponse(
                res.getId(), "SUCCESS", "Extras actualizados correctamente");
    }

    /* Helper para respuestas de error */
    private SolicitarExtrasReservaResponse fail(SolicitarExtrasReservaCommand c, String msg) {
        return new SolicitarExtrasReservaResponse(c.idReserva(), "FAILURE", msg);
    }
}