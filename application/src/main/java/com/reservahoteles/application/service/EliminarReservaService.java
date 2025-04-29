package com.reservahoteles.application.service;

import com.reservahoteles.application.dto.EliminarReservaCommand;
import com.reservahoteles.application.dto.EliminarReservaResponse;
import com.reservahoteles.application.port.in.EliminarReservaUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.domain.entity.Reserva;
import com.reservahoteles.domain.port.out.ReservaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
public class EliminarReservaService implements EliminarReservaUseCase {

    private final ReservaRepository reservaRepo;

    public EliminarReservaService(ReservaRepository reservaRepo) {
        this.reservaRepo = reservaRepo;
    }

    @Override
    public EliminarReservaResponse ejecutar(EliminarReservaCommand cmd) {
        // 1) Cargar reserva o 404
        Reserva res = reservaRepo.findById(cmd.idReserva())
                .orElseThrow(() -> new NoSuchElementException(
                        "No existe la reserva id=" + cmd.idReserva()
                ));

        boolean esAdmin  = cmd.rolActor() == Rol.ADMINISTRADOR;
        boolean esPropia = Objects.equals(res.getIdCliente(), cmd.idActor());

        // 2) Permisos
        if (!esAdmin && !esPropia) {
            return new EliminarReservaResponse(
                    cmd.idReserva(),
                    "FAILURE",
                    "No tiene permiso para eliminar esta reserva"
            );
        }

        // 3) Reglas para el cliente (propietario)
//        if (!esAdmin) {
//            // Solo puede borrar sus propias reservas CANCELADAS
//            if (res.getEstadoReserva() != EstadoReserva.CANCELADA) {
//                return new EliminarReservaResponse(
//                        cmd.idReserva(),
//                        "FAILURE",
//                        "Solo puede eliminar reservas que ya estén canceladas"
//                );
//            }
//            // Y siempre antes de la fecha de entrada
//            if (!LocalDate.now().isBefore(res.getFechaEntrada())) {
//                return new EliminarReservaResponse(
//                        cmd.idReserva(),
//                        "FAILURE",
//                        "Ya no puede eliminar una reserva cuyo check‑in comenzó"
//                );
//            }
//        }

        // 4) Eliminar (hard delete o soft‑delete en repo)
        reservaRepo.eliminar(cmd.idReserva());

        return new EliminarReservaResponse(
                cmd.idReserva(),
                "SUCCESS",
                "Reserva eliminada correctamente"
        );
    }
}