package com.reservahoteles.application.service;
import com.reservahoteles.application.dto.EliminarHotelCommand;
import com.reservahoteles.application.dto.EliminarHotelResponse;
import com.reservahoteles.application.port.in.EliminarHotelUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.domain.entity.Habitacion;
import com.reservahoteles.domain.entity.Reserva;
import com.reservahoteles.domain.port.out.HabitacionRepository;
import com.reservahoteles.domain.port.out.HotelRepository;
import com.reservahoteles.domain.port.out.ReservaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class EliminarHotelService implements EliminarHotelUseCase {

    private final HotelRepository      hotelRepo;
    private final HabitacionRepository habitacionRepo;
    private final ReservaRepository    reservaRepo;

    @Override
    public EliminarHotelResponse ejecutar(EliminarHotelCommand cmd) {
        // 1) Solo el ADMIN puede eliminar hoteles
        if (cmd.rolActor() != Rol.ADMINISTRADOR) {
            return new EliminarHotelResponse(
                    cmd.idHotel(),
                    "FAILURE",
                    "Solo el administrador puede eliminar hoteles"
            );
        }

        // 2) Verificar existencia del hotel
        hotelRepo.findById(cmd.idHotel())
                .orElseThrow(() ->
                        new NoSuchElementException("No se encontró el hotel id=" + cmd.idHotel())
                );

        // 3) Obtener todas las habitaciones del hotel (paginado sin filtros)
        List<Habitacion> habitaciones = habitacionRepo.buscarPorHotel(
                cmd.idHotel(),
                /*estado=*/ null,
                /*tipo=*/   null,
                Pageable.unpaged()
        ).getContent();

        // 4) Revisar reservas activas o futuras en cada habitación
        LocalDate hoy = LocalDate.now();
        for (Habitacion h : habitaciones) {
            List<Reserva> reservas = reservaRepo.buscarPorCriteriosAvanzados(
                    /*idCliente=*/       null,
                    /*idHabitacion=*/    h.getId(),
                    /*fechaInicio=*/     hoy,
                    /*fechaFin=*/        null,
                    /*estadoReserva=*/   null,
                    Pageable.unpaged()
            ).getContent();

            boolean tieneActivas = reservas.stream()
                    .anyMatch(r ->
                            r.getEstadoReserva() != EstadoReserva.CANCELADA &&
                                    r.getEstadoReserva() != EstadoReserva.FINALIZADA
                    );
            if (tieneActivas) {
                return new EliminarHotelResponse(
                        cmd.idHotel(),
                        "FAILURE",
                        "No se puede eliminar el hotel: hay reservas activas o pendientes en la habitación "
                                + h.getNumeroHabitacion()
                );
            }
        }

        // 5) Finalmente, eliminar (o desactivar) el hotel
        hotelRepo.eliminar(cmd.idHotel());

        return new EliminarHotelResponse(
                cmd.idHotel(),
                "SUCCESS",
                "Hotel eliminado correctamente"
        );
    }
}