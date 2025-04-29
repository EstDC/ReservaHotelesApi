package com.reservahoteles.application.service;
import com.reservahoteles.application.dto.NotificarCambiosReservasCommand;
import com.reservahoteles.application.dto.NotificarCambiosReservasResponse;
import com.reservahoteles.application.port.in.NotificarCambiosReservasUseCase;
import com.reservahoteles.domain.event.ReservationChangeEvent;
import com.reservahoteles.domain.entity.Reserva;
import com.reservahoteles.domain.port.out.NotificationPublisher;
import com.reservahoteles.domain.port.out.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class NotificarCambiosReservasService implements NotificarCambiosReservasUseCase {

    private final ReservaRepository reservaRepo;
    private final NotificationPublisher notifier;

    public NotificarCambiosReservasService(ReservaRepository reservaRepo,
                                           NotificationPublisher notifier) {
        this.reservaRepo = reservaRepo;
        this.notifier    = notifier;
    }

    @Override
    public NotificarCambiosReservasResponse ejecutar(NotificarCambiosReservasCommand cmd) {
        // 1) Validar comando
        if (cmd.idReserva() == null) {
            return new NotificarCambiosReservasResponse(
                    null, "FAILURE", "idReserva es obligatorio"
            );
        }

        try {
            // 2) Recuperar la reserva (solo para asegurarnos de que existe)
            Reserva reserva = reservaRepo.findById(cmd.idReserva())
                    .orElseThrow(() -> new NoSuchElementException(
                            "No existe la reserva con id = " + cmd.idReserva()
                    ));

//             3 Opcional: controlar que solo el admin o el propio cliente puedan disparar
//                Si quieres restricciones, descomenta y ajusta:
//
//            boolean esAdmin  = cmd.rolActor() == Rol.ADMINISTRADOR;
//            boolean esPropio = reserva.getIdCliente().equals(cmd.idActor());
//            if (!esAdmin && !esPropio) {
//                return new NotificarCambiosReservasResponse(
//                    cmd.idReserva(), "FAILURE",
//                    "No tiene permisos para notificar cambios en esta reserva"
//                );
//            }


            // 4) Crear y publicar evento
            notifier.publishReservationChange(
                    new ReservationChangeEvent(
                            reserva.getId(),
                            reserva.getEstadoReserva(),
                            LocalDateTime.now()
                    )
            );

            return new NotificarCambiosReservasResponse(
                    reserva.getId(),
                    "SUCCESS",
                    "Notificación enviada correctamente"
            );

        } catch (NoSuchElementException e) {
            return new NotificarCambiosReservasResponse(
                    cmd.idReserva(), "FAILURE", e.getMessage()
            );
        } catch (Exception e) {
            // capturamos cualquier fallo de publisher u otro
            return new NotificarCambiosReservasResponse(
                    cmd.idReserva(), "FAILURE",
                    "Error al notificar: " + e.getMessage()
            );
        }
    }
}

//Validación de entrada
//
//idReserva no puede ser null.
//
//Si necesitas controlar quién dispara la notificación, puedes descomentar la sección de restricciones (esAdmin / esPropio).
//
//Existencia de la reserva
//
//Antes de notificar, comprobamos que la reserva existe en la base de datos.
//
//Publicación del evento
//
//Construimos un ReservationChangeEvent con el estado actual de la reserva y la hora, y lo enviamos al NotificationPublisher que tengas configurado (SSE, WebSocket, etc.).
//
//Manejo de errores
//
//Si la reserva no existe, devolvemos FAILURE con el mensaje.
//
//Si falla el publisher o cualquier otra cosa, capturamos la excepción y devolvemos un FAILURE genérico.