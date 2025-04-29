package com.reservahoteles.application.service;

import com.reservahoteles.application.dto.ActualizarClienteBancarioCommand;
import com.reservahoteles.application.dto.ActualizarClienteBancarioResponse;
import com.reservahoteles.application.port.in.ActualizarClienteBancarioUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Pago;
import com.reservahoteles.domain.port.out.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActualizarClienteBancarioService implements ActualizarClienteBancarioUseCase {

    private final PagoRepository pagoRepo;

    @Override
    public ActualizarClienteBancarioResponse ejecutar(ActualizarClienteBancarioCommand cmd) {
        Pago formaPago = pagoRepo.findById(cmd.idFormaPago())
                .orElse(null);

        if (formaPago == null || !formaPago.isActivo()) {
            return new ActualizarClienteBancarioResponse(null, "FAILURE", "Forma de pago no encontrada o inactiva.");
        }

        // Verificar que el actor sea el dueño o admin
        if (!formaPago.getIdCliente().equals(cmd.idActor()) && cmd.rolActor() != Rol.ADMINISTRADOR) {
            return new ActualizarClienteBancarioResponse(cmd.idFormaPago(), "FAILURE", "No autorizado para modificar este método de pago.");
        }

        // Actualizar datos
        formaPago.setTipoPago(cmd.tipoPago());
        formaPago.setNumeroCuenta(cmd.numeroCuenta());
        formaPago.setTitular(cmd.titular());
        formaPago.setExpiracion(cmd.expiracion());
        formaPago.setOtrosDetalles(cmd.otrosDetalles());
        formaPago.setUltimaActualizacion(LocalDateTime.now());

        pagoRepo.save(formaPago);

        return new ActualizarClienteBancarioResponse(cmd.idFormaPago(), "SUCCESS", "Método de pago actualizado.");
    }
}