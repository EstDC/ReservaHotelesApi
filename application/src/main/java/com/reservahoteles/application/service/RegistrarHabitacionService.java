package com.reservahoteles.application.service;
import com.reservahoteles.application.dto.RegistrarHabitacionCommand;
import com.reservahoteles.application.dto.RegistrarHabitacionResponse;
import com.reservahoteles.application.port.in.RegistrarHabitacionUseCase;
import com.reservahoteles.common.enums.EstadoHabitacion;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Habitacion;
import com.reservahoteles.domain.port.out.HabitacionRepository;
import com.reservahoteles.domain.port.out.HotelRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

@Service
@Transactional
public class RegistrarHabitacionService implements RegistrarHabitacionUseCase {

    private final HotelRepository hotelRepo;
    private final HabitacionRepository habitacionRepo;

    // nº de habitación alfanumérico y guiones
    private static final Pattern NUMERO_REGEX = Pattern.compile("^[A-Za-z0-9\\-]+$");

    public RegistrarHabitacionService(HotelRepository hotelRepo,
                                      HabitacionRepository habitacionRepo) {
        this.hotelRepo       = hotelRepo;
        this.habitacionRepo  = habitacionRepo;
    }

    @Override
    public RegistrarHabitacionResponse ejecutar(RegistrarHabitacionCommand cmd) {
        // 1) Solo ADMIN
        if (cmd.rolActor() != Rol.ADMINISTRADOR) {
            return new RegistrarHabitacionResponse(
                    null, "FAILURE",
                    "Solo el administrador puede crear habitaciones"
            );
        }
        // 2) Campos obligatorios
        if (cmd.idHotel() == null) {
            return fail(null, "Debes indicar el hotel al que pertenece");
        }
        if (cmd.numeroHabitacion() == null || cmd.numeroHabitacion().isBlank()) {
            return fail(null, "El número de habitación es obligatorio");
        }
        if (cmd.tipo() == null) {
            return fail(null, "El tipo de habitación es obligatorio");
        }
        if (cmd.capacidad() == null || cmd.capacidad() <= 0) {
            return fail(null, "La capacidad debe ser un entero mayor que cero");
        }
        if (cmd.precioPorNoche() == null || cmd.precioPorNoche().compareTo(BigDecimal.ZERO) < 0) {
            return fail(null, "El precio por noche debe ser ≥ 0");
        }
        if (cmd.estado() == null) {
            return fail(null, "El estado de la habitación es obligatorio");
        }
        // 3) No permitir crear ya reservada
        if (cmd.estado() == EstadoHabitacion.RESERVADA) {
            return fail(null, "No tiene sentido crear una habitación ya RESERVADA");
        }

        // 4) El hotel debe existir
        hotelRepo.findById(cmd.idHotel())
                .orElseThrow(() -> new NoSuchElementException(
                        "No existe el hotel con id = " + cmd.idHotel()
                ));

        // 5) Formato del número
        if (!NUMERO_REGEX.matcher(cmd.numeroHabitacion()).matches()) {
            return fail(null, "Formato inválido para número de habitación");
        }

        // 6) Unicidad dentro del hotel
        boolean duplicada = habitacionRepo
                .buscarPorCriterios(
                        cmd.idHotel(),           // hotel
                        cmd.numeroHabitacion(),  // número a verificar
                        null,                    // capacidad (sin filtrar)
                        null,                    // precio mínimo (sin filtrar)
                        null                     // estado (sin filtrar)
                )
                .stream()
                .anyMatch(h -> h.getNumeroHabitacion()
                        .equalsIgnoreCase(cmd.numeroHabitacion()));
        if (duplicada) {
            return fail(null, "Ya existe una habitación con ese número en el hotel");
        }

        // 7) Construir y persistir
        Habitacion hab = Habitacion.builder()
                .idHotel(cmd.idHotel())
                .numeroHabitacion(cmd.numeroHabitacion().trim())
                .tipo(cmd.tipo())
                .capacidad(cmd.capacidad())
                .precioPorNoche(cmd.precioPorNoche())
                .descripcion(cmd.descripcion())
                .estado(cmd.estado())
                .build();

        Habitacion creada = habitacionRepo.save(hab);

        return new RegistrarHabitacionResponse(
                creada.getId(),
                "SUCCESS",
                "Habitación creada correctamente (id=" + creada.getId() + ")"
        );
    }

    private RegistrarHabitacionResponse fail(Long id, String msg) {
        return new RegistrarHabitacionResponse(id, "FAILURE", msg);
    }
}