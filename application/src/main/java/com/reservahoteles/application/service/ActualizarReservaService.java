package com.reservahoteles.application.service;

import com.reservahoteles.application.dto.ActualizarReservaCommand;
import com.reservahoteles.application.dto.ActualizarReservaResponse;
import com.reservahoteles.application.port.in.ActualizarReservaUseCase;
import com.reservahoteles.common.enums.EstadoHabitacion;
import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Extra;
import com.reservahoteles.domain.entity.Reserva;
import com.reservahoteles.domain.port.out.ExtraRepository;
import com.reservahoteles.domain.port.out.HabitacionRepository;
import com.reservahoteles.domain.port.out.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class ActualizarReservaService implements ActualizarReservaUseCase {

    private final ReservaRepository reservaRepo;
    private final HabitacionRepository habitacionRepo;
    private final ExtraRepository extraRepo;
    private final NotificacionReservaService notificacionService;

    public ActualizarReservaService(ReservaRepository reservaRepo,
                                  HabitacionRepository habitacionRepo,
                                  ExtraRepository extraRepo,
                                  NotificacionReservaService notificacionService) {
        this.reservaRepo = reservaRepo;
        this.habitacionRepo = habitacionRepo;
        this.extraRepo = extraRepo;
        this.notificacionService = notificacionService;
    }

    @Override
    public ActualizarReservaResponse ejecutar(ActualizarReservaCommand cmd) {
        /* 1 ── Cargar reserva */
        Reserva res = reservaRepo.findById(cmd.idReserva())
                .orElseThrow(() -> new NoSuchElementException(
                        "Reserva no encontrada (id = " + cmd.idReserva() + ')'));

        boolean esAdmin = cmd.rolActor() == Rol.ADMINISTRADOR;
        boolean esPropia = Objects.equals(res.getIdCliente(), cmd.idActor());

        /* 2 ── Seguridad básica */
        if (!esAdmin && !esPropia) {
            return fail(cmd, "No tiene permisos para modificar esta reserva");
        }

        boolean cambiado = false;
        EstadoReserva estadoAnterior = res.getEstadoReserva();

        // ───────────────────────────  BLOQUE ADMIN  ─────────────────────────
        if (esAdmin) {
            /* 2.a  Cambiar fechas / habitación */
            if (cmd.fechaEntrada() != null || cmd.fechaSalida() != null) {
                LocalDate fEntrada = Optional.ofNullable(cmd.fechaEntrada()).orElse(res.getFechaEntrada());
                LocalDate fSalida = Optional.ofNullable(cmd.fechaSalida()).orElse(res.getFechaSalida());

                if (!fEntrada.isBefore(fSalida)) {
                    return fail(cmd, "La fecha de entrada debe ser anterior a la de salida");
                }

                // Verificar solape
                boolean haySolape = reservaRepo.buscarPorCriteriosAvanzados(
                                null,                             /* idCliente   */
                                res.getIdHabitacion(),            /* habitación  */
                                fEntrada, fSalida, null)          /* estado      */
                        .stream()
                        .anyMatch(r -> !r.getId().equals(res.getId())); // excluirse

                if (haySolape) {
                    return fail(cmd, "Las fechas se solapan con otra reserva");
                }

                res.setFechaEntrada(fEntrada);
                res.setFechaSalida(fSalida);
                cambiado = true;
            }

            if (cmd.idHabitacion() != null && !cmd.idHabitacion().equals(res.getIdHabitacion())) {
                habitacionRepo.findById(cmd.idHabitacion()).orElseThrow(
                        () -> new IllegalArgumentException("La habitación no existe"));
                res.setIdHabitacion(cmd.idHabitacion());
                cambiado = true;
            }

            /* 2.b  Cambio de estado libre (con una tabla de transiciones sencilla) */
            if (cmd.estadoReserva() != null) {
                if (!transicionAdmin(res.getEstadoReserva(), cmd.estadoReserva())) {
                    return fail(cmd, "Transición de estado no permitida");
                }
                res.setEstadoReserva(cmd.estadoReserva());
                cambiado = true;
            }
        }

        // ───────────────────────  BLOQUE CLIENTE/ADMIN  ─────────────────────
        /* 3 ── Extras (admin o cliente propietario) */
        if (cmd.idsExtras() != null) {
            List<Extra> extras = new ArrayList<>();
            for (Long idExtra : cmd.idsExtras()) {
                extras.add(extraRepo.findById(idExtra)
                        .orElseThrow(() -> new IllegalArgumentException("Extra no encontrado: " + idExtra)));
            }
            res.setExtras(extras);
            cambiado = true;
        }

        if (cambiado) {
            reservaRepo.save(res);
            // Notificar el cambio de estado si hubo uno
            if (cmd.estadoReserva() != null && !cmd.estadoReserva().equals(estadoAnterior)) {
                notificacionService.notificarCambioReserva(res.getId(), cmd.estadoReserva());
            }
            return new ActualizarReservaResponse(res.getId(), "SUCCESS", "Reserva actualizada");
        }

        return new ActualizarReservaResponse(res.getId(), "SUCCESS", "No hubo cambios");
    }

    private ActualizarReservaResponse fail(ActualizarReservaCommand cmd, String msg) {
        return new ActualizarReservaResponse(cmd.idReserva(), "ERROR", msg);
    }

    private boolean transicionAdmin(EstadoReserva actual, EstadoReserva nuevo) {
        // Tabla de transiciones permitidas para admin
        Map<EstadoReserva, Set<EstadoReserva>> transiciones = Map.of(
                EstadoReserva.PENDIENTE, Set.of(EstadoReserva.CONFIRMADA, EstadoReserva.CANCELADA),
                EstadoReserva.CONFIRMADA, Set.of(EstadoReserva.EN_CURSO, EstadoReserva.CANCELADA),
                EstadoReserva.EN_CURSO, Set.of(EstadoReserva.FINALIZADA),
                EstadoReserva.FINALIZADA, Set.of(EstadoReserva.ARCHIVADA),
                EstadoReserva.CANCELADA, Set.of(EstadoReserva.ARCHIVADA)
        );
        return transiciones.getOrDefault(actual, Set.of()).contains(nuevo);
    }
}