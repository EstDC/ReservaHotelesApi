package com.reservahoteles.application.service;

import com.reservahoteles.application.dto.ActualizarHabitacionCommand;
import com.reservahoteles.application.dto.ActualizarHabitacionResponse;
import com.reservahoteles.application.port.in.ActualizarHabitacionUseCase;
import com.reservahoteles.common.enums.EstadoHabitacion;
import com.reservahoteles.domain.entity.Extra;
import com.reservahoteles.domain.entity.Habitacion;
import com.reservahoteles.domain.port.out.ExtraRepository;
import com.reservahoteles.domain.port.out.HabitacionRepository;
import com.reservahoteles.domain.port.out.HotelRepository;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.math.BigDecimal;

@Service
public class ActualizarHabitacionService implements ActualizarHabitacionUseCase {

    private final HabitacionRepository habitacionRepo;
    private final HotelRepository     hotelRepo;
    private final ExtraRepository     extraRepo;

    // patrón muuuuy básico para tipo "A-123" (solo si decides validar el número).
    private static final Pattern NUMERO_REGEX = Pattern.compile("^[A-Za-z0-9\\-]+$");

    public ActualizarHabitacionService(HabitacionRepository habitacionRepo,
                                       HotelRepository hotelRepo,
                                       ExtraRepository extraRepo) {
        this.habitacionRepo = habitacionRepo;
        this.hotelRepo      = hotelRepo;
        this.extraRepo      = extraRepo;
    }

    @Override
    public ActualizarHabitacionResponse ejecutar(ActualizarHabitacionCommand cmd) {

        /* 1. Cargar la habitación; 404 si no existe */
        Habitacion hab = habitacionRepo.findById(cmd.idHabitacion())
                .orElseThrow(() -> new NoSuchElementException(
                        "No existe la habitación con id = " + cmd.idHabitacion())
                );

        /* 2. Si se pretende mover de hotel, verificar que el nuevo hotel existe */
        Long idHotelDestino = cmd.idHotel() != null ? cmd.idHotel() : hab.getIdHotel();
        hotelRepo.findById(idHotelDestino).orElseThrow(() -> new IllegalArgumentException(
                "El hotel destino no existe (id = " + idHotelDestino + ")"
        ));

        boolean cambiado = false;

        /* 3. Número de habitación */
        if (cmd.numeroHabitacion() != null
                && !cmd.numeroHabitacion().equalsIgnoreCase(hab.getNumeroHabitacion())) {

            if (!NUMERO_REGEX.matcher(cmd.numeroHabitacion()).matches()) {
                return fail(cmd, "Número de habitación con formato inválido.");
            }

            // Único por hotel:
            boolean existeNumero = habitacionRepo
                    .buscarPorCriterios(idHotelDestino,
                            cmd.numeroHabitacion(),
                            null, null, null)
                    .stream()
                    .anyMatch(h -> !h.getId().equals(cmd.idHabitacion()));

            if (existeNumero) {
                return fail(cmd, "Ya existe la habitación '" +
                        cmd.numeroHabitacion() + "' en ese hotel.");
            }
            hab.setNumeroHabitacion(cmd.numeroHabitacion());
            cambiado = true;
        }

        /* 4 ── Tipo / capacidad */
        if (cmd.tipo() != null) { hab.setTipo(cmd.tipo()); cambiado = true; }
        if (cmd.capacidad() != null) {
            if (cmd.capacidad() <= 0) return fail(cmd, "La capacidad debe ser > 0.");
            hab.setCapacidad(cmd.capacidad()); cambiado = true;
        }

        /* 5. Precio */
        if (cmd.precioPorNoche() != null) {
            if (cmd.precioPorNoche().compareTo(BigDecimal.ZERO) < 0) return fail(cmd, "El precio no puede ser negativo.");
            hab.setPrecioPorNoche(cmd.precioPorNoche()); cambiado = true;
        }

        /* 6. Estado (regla de transición simple) */
        if (cmd.estado() != null && cmd.estado() != hab.getEstado()) {
            if (!transicionEstadoValida(hab.getEstado(), cmd.estado())) {
                return fail(cmd, "Transición de estado inválida (" +
                        hab.getEstado() + " → " + cmd.estado() + ")");
            }
            hab.setEstado(cmd.estado());
            cambiado = true;
        }

        /* 7 ── Descripción */
        if (cmd.descripcion() != null) {
            hab.setDescripcion(cmd.descripcion());
            cambiado = true;
        }

        /* 8 ── Extras (reemplazo completo) */
        if (cmd.idsExtras() != null) {
            List<Extra> extrasSeleccionados = extraRepo.findByIds(cmd.idsExtras());

            if (extrasSeleccionados.size() != cmd.idsExtras().size()) {
                return fail(cmd, "Alguno de los extras enviados no existe.");
            }
            hab.setExtras(extrasSeleccionados);
            cambiado = true;
        }

        /* 9. Mover de hotel si corresponde */
        if (!Objects.equals(hab.getIdHotel(), idHotelDestino)) {
            hab.setIdHotel(idHotelDestino);
            cambiado = true;
        }

        /* 10 ── Si no cambió nada */
        if (!cambiado) {
            return fail(cmd, "No se indicó ningún dato para actualizar.");
        }

        /* 11 ── Persistir */
        habitacionRepo.save(hab);

        return new ActualizarHabitacionResponse(
                hab.getId(), "SUCCESS", "Habitación actualizada correctamente");
    }

    // Helpers ----------------------------------------------------------------
    private ActualizarHabitacionResponse fail(ActualizarHabitacionCommand cmd, String msg) {
        return new ActualizarHabitacionResponse(cmd.idHabitacion(), "FAILURE", msg);
    }

    /** Solo se permiten las transiciones DISPONIBLE ↔ MANTENIMIENTO.
     *  OCUPADA se asigna desde lógica de reservas.              */
    private boolean transicionEstadoValida(EstadoHabitacion actual, EstadoHabitacion nuevo) {
        return switch (actual) {
            case DISPONIBLE -> nuevo == EstadoHabitacion.MANTENIMIENTO;
            case MANTENIMIENTO -> nuevo == EstadoHabitacion.DISPONIBLE;
            case RESERVADA -> false;           // la ocupa/ libera el flujo de reservas
        };
    }
}


//UI carga la ficha de la habitación
//
//El usuario (admin) edita campos → envía ActualizarHabitacionCommand
//
//ActualizarHabitacionService
//
//        válida existencia, unicidad de número, reglas de estado, precio positivo…
//
//opcionalmente mueve la habitación a otro hotel
//
//reemplaza extras si se pasan ids
//
//Persiste y devuelve un ActualizarHabitacionResponse con SUCCESS/FAILURE y mensaje.