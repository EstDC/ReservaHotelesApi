package com.reservahoteles.application.service;
import com.reservahoteles.application.dto.EliminarHabitacionCommand;
import com.reservahoteles.application.dto.EliminarHabitacionResponse;
import com.reservahoteles.application.port.in.EliminarHabitacionUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.domain.entity.Habitacion;
import com.reservahoteles.domain.entity.Reserva;
import com.reservahoteles.domain.port.out.HabitacionRepository;
import com.reservahoteles.domain.port.out.ReservaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@Transactional
public class EliminarHabitacionService implements EliminarHabitacionUseCase {

    private final HabitacionRepository habitacionRepo;
    private final ReservaRepository    reservaRepo;

    public EliminarHabitacionService(HabitacionRepository habitacionRepo,
                                     ReservaRepository reservaRepo) {
        this.habitacionRepo = habitacionRepo;
        this.reservaRepo    = reservaRepo;
    }

    @Override
    public EliminarHabitacionResponse ejecutar(EliminarHabitacionCommand cmd) {
        // 1) Solo el ADMIN puede eliminar habitaciones
        if (cmd.rolActor() != Rol.ADMINISTRADOR) {
            return failure(cmd, "Solo el administrador puede eliminar habitaciones");
        }

        // 2) Verificar que la habitación existe
        Habitacion hab = habitacionRepo.findById(cmd.idHabitacion())
                .orElseThrow(() ->
                        new NoSuchElementException("No se encontró la habitación id=" + cmd.idHabitacion())
                );

        // 3) Comprobar que no haya reservas activas o futuras para esta habitación
        //    (estado distinto de CANCELADA/FINALIZADA y fechaSalida >= hoy)
        List<Reserva> ocupaciones = reservaRepo.buscarPorCriteriosAvanzados(
                null,                               // idCliente irrelevante
                hab.getId(),                        // idHabitacion
                LocalDate.now(),                    // fechaInicio = hoy
                null,                               // fechaFin = sin tope
                null                                // estadoReserva opcional
        );
        boolean tieneActivas = ocupaciones.stream()
                .anyMatch(r -> r.getEstadoReserva() != EstadoReserva.CANCELADA
                        && r.getEstadoReserva() != EstadoReserva.FINALIZADA);
        if (tieneActivas) {
            return failure(cmd,
                    "No se puede eliminar: existen reservas activas o pendientes para esta habitación");
        }

        // 4) Eliminar (o desactivar) la habitación
        habitacionRepo.eliminar(hab.getId());

        return new EliminarHabitacionResponse(
                hab.getId(),
                "SUCCESS",
                "Habitación eliminada correctamente"
        );
    }

    private EliminarHabitacionResponse failure(EliminarHabitacionCommand cmd, String msg) {
        return new EliminarHabitacionResponse(
                cmd.idHabitacion(),
                "FAILURE",
                msg
        );
    }
}

//Permisos
//Solo un usuario con rol ADMINISTRADOR puede eliminar una habitación.
//
//Existencia
//Se lanza NoSuchElementException si el idHabitacion no existe.
//
//Validación de reservas
//Usamos reservaRepo.buscarPorCriteriosAvanzados buscando reservas de hoy en adelante para esa habitación. Si alguna no está en estado CANCELADA o FINALIZADA, bloqueamos la eliminación.
//
//        Eliminación
//Llamamos a habitacionRepo.eliminar(id). En tu implementación puedes hacerlo como un hard delete o marcar un flag activo=false si prefieres soft‑delete.
//
//        Respuesta
//Devolvemos un status y un mensaje adecuado.