package com.reservahoteles.application.service;
import com.reservahoteles.application.dto.RegistrarPagoCommand;
import com.reservahoteles.application.dto.RegistrarPagoResponse;
import com.reservahoteles.application.port.in.RegistrarPagoUseCase;
import com.reservahoteles.common.enums.FormaPago;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.common.enums.EstadoPago;
import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.domain.entity.Pago;
import com.reservahoteles.domain.entity.Reserva;
import com.reservahoteles.domain.port.out.PagoRepository;
import com.reservahoteles.domain.port.out.ReservaRepository;
import com.reservahoteles.domain.port.out.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
@Transactional
public class RegistrarPagoService implements RegistrarPagoUseCase {

    private static final Pattern EXPIRACION_RX = Pattern.compile("^(0[1-9]|1[0-2])/[0-9]{2}$");

    private final PagoRepository pagoRepo;
    private final ReservaRepository reservaRepo;
    private final ClienteRepository clienteRepo;

    public RegistrarPagoService(PagoRepository pagoRepo,
                                ReservaRepository reservaRepo,
                                ClienteRepository clienteRepo) {
        this.pagoRepo    = pagoRepo;
        this.reservaRepo = reservaRepo;
        this.clienteRepo = clienteRepo;
    }

    @Override
    public RegistrarPagoResponse ejecutar(RegistrarPagoCommand cmd) {
        LocalDateTime ahora = LocalDateTime.now();

        // 1) Permisos: sólo cliente propietario o admin
        boolean esAdmin = cmd.rolActor() == Rol.ADMINISTRADOR;
        if (!esAdmin && !Objects.equals(cmd.idActor(), cmd.idCliente())) {
            return new RegistrarPagoResponse(
                    null, "FAILURE",
                    "No tiene permiso para realizar este pago"
            );
        }

        // 2) Validar reserva existe y pertenece al cliente
        Reserva res = reservaRepo.findById(cmd.idReserva())
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró la reserva id=" + cmd.idReserva()
                ));
        if (!esAdmin && !Objects.equals(res.getIdCliente(), cmd.idCliente())) {
            return new RegistrarPagoResponse(
                    null, "FAILURE",
                    "La reserva no pertenece al cliente indicado"
            );
        }

        // 3) No permitir pagos sobre reservas canceladas o finalizadas
        if (res.getEstadoReserva() == EstadoReserva.CANCELADA
                || res.getEstadoReserva() == EstadoReserva.FINALIZADA) {
            return new RegistrarPagoResponse(
                    null, "FAILURE",
                    "No se puede pagar una reserva " + res.getEstadoReserva()
            );
        }

        // 4) Validar cliente existe
        clienteRepo.findById(cmd.idCliente())
                .orElseThrow(() -> new NoSuchElementException(
                        "No existe el cliente id=" + cmd.idCliente()
                ));

        // 5) Validar monto
        if (cmd.monto() == null || cmd.monto().compareTo(BigDecimal.ZERO) <= 0) {
            return new RegistrarPagoResponse(
                    null, "FAILURE",
                    "El monto debe ser mayor que cero"
            );
        }

        // 6) Determinar y validar tipo de pago
        if (cmd.formaPago() == null) {
            return new RegistrarPagoResponse(
                    null, "FAILURE",
                    "Debe indicar un método de pago"
            );
        }
        FormaPago tipoPago = cmd.formaPago();

        // 7) Si es TARJETA, validar datos asociados
        if (tipoPago == FormaPago.TARJETA) {
            if (cmd.numeroCuenta() == null || cmd.numeroCuenta().length() < 4) {
                return new RegistrarPagoResponse(
                        null, "FAILURE",
                        "Debe indicar los últimos 4 dígitos de la tarjeta"
                );
            }
            if (cmd.titular() == null || cmd.titular().isBlank()) {
                return new RegistrarPagoResponse(
                        null, "FAILURE",
                        "Debe indicar el nombre del titular"
                );
            }
            if (cmd.expiracion() == null || !EXPIRACION_RX.matcher(cmd.expiracion()).matches()) {
                return new RegistrarPagoResponse(
                        null, "FAILURE",
                        "Formato de expiración inválido (MM/AA)"
                );
            }
        }

        // 8) Estado inicial ficticio según tipo de pago
        EstadoPago estadoInicial = (tipoPago == FormaPago.TARJETA)
                ? EstadoPago.COMPLETADO
                : EstadoPago.PENDIENTE;

        // 9) Construir entidad de dominio
        Pago nuevoPago = Pago.builder()
                .idReserva(cmd.idReserva())
                .idCliente(cmd.idCliente())
                .monto(cmd.monto())
                .fechaPago(ahora)
                .estadoPago(estadoInicial)
                .otrosDetalles(cmd.otrosDetalles())
                .tipoPago(tipoPago)
                .numeroCuenta(cmd.numeroCuenta())
                .titular(cmd.titular())
                .expiracion(cmd.expiracion())
                .fechaRegistro(ahora)
                .ultimaActualizacion(ahora)
                .activo(true)
                .build();

        // 10) Persistir y responder
        Pago guardado = pagoRepo.save(nuevoPago);

        return new RegistrarPagoResponse(
                guardado.getId(),
                "SUCCESS",
                "Pago registrado con estado " + estadoInicial
        );
    }
}