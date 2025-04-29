package com.reservahoteles.application.service;

import com.reservahoteles.application.dto.*;
import com.reservahoteles.application.port.in.AsignarServiciosHotelUseCase;
import com.reservahoteles.common.enums.ModoAsignacion;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Hotel;
import com.reservahoteles.domain.entity.Servicio;
import com.reservahoteles.domain.event.ReservationChangeEvent;
import com.reservahoteles.domain.port.out.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AsignarServiciosHotelService implements AsignarServiciosHotelUseCase {

    private final HotelRepository      hotelRepo;
    private final ServicioRepository   servicioRepo;
    private final NotificationPublisher notifier;      // opcional (SSE)

    @Override
    public AsignarServiciosHotelResponse ejecutar(AsignarServiciosHotelCommand cmd) {

        /* 1 ── Seguridad: solo ADMINISTRADOR ─────────────────────────── */
        if (cmd.rolActor() != Rol.ADMINISTRADOR) {
            return fail(cmd, "Permiso denegado: se requiere rol ADMINISTRADOR");
        }

        /* 2 ── Hotel existente ───────────────────────────────────────── */
        Hotel hotel = hotelRepo.findById(cmd.idHotel())
                .orElseThrow(() -> new NoSuchElementException(
                        "Hotel no encontrado (id=" + cmd.idHotel() + ')'));

        /* 3 ── Validar servicios recibidos ───────────────────────────── */
        List<Servicio> serviciosSolicitados = servicioRepo.findByIds(cmd.idsServicios());
        if (serviciosSolicitados.size() != cmd.idsServicios().size()) {
            return fail(cmd, "Alguno de los servicios indicados no existe");
        }

        /* 4 ── Copia mutable de la lista actual ──────────────────────── */
        List<Servicio> listaActual = hotel.getServicios() == null
                ? new ArrayList<>()
                : new ArrayList<>(hotel.getServicios());

        /* 5 ── Aplicar operación según modo ─────────────────────────── */
        ModoAsignacion modo = cmd.modo() != null ? cmd.modo() : ModoAsignacion.REEMPLAZAR;

        switch (modo) {
            case REEMPLAZAR -> {
                listaActual.clear();
                listaActual.addAll(serviciosSolicitados);
            }
            case AGREGAR -> {
                Set<Long> idsActuales = listaActual.stream()
                        .map(Servicio::getId).collect(Collectors.toSet());
                serviciosSolicitados.stream()
                        .filter(s -> !idsActuales.contains(s.getId()))
                        .forEach(listaActual::add);
            }
            case QUITAR -> listaActual.removeIf(serviciosSolicitados::contains);
        }

        hotel.setServicios(listaActual);

        /* 6 ── (opcional) Recalcular rating/categoría de servicios ——  */
        int puntosServicios = listaActual.size();          // métrica trivial de ejemplo
        hotel.setNumeroEstrellas(Math.min(5, 3 + puntosServicios / 5));

        /* 7 ── Persistir y notificar ───────────────────────────────── */
        hotelRepo.save(hotel);

        notifier.publishReservationChange(                 // se puede omitir si no lo usas
                new ReservationChangeEvent(
                        hotel.getId(),                     // usamos idHotel como “id reserva” ficticio
                        null,                              // sin estado
                        LocalDateTime.now())
        );

        return new AsignarServiciosHotelResponse(
                hotel.getId(), "SUCCESS",
                "Servicios actualizados (" + modo + "), total = " + listaActual.size());
    }

    // helper --------------------------------------------------------------
    private AsignarServiciosHotelResponse fail(AsignarServiciosHotelCommand c, String msg) {
        return new AsignarServiciosHotelResponse(c.idHotel(), "FAILURE", msg);
    }
}