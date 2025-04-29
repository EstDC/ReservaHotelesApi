package com.reservahoteles.application.service;

import com.reservahoteles.application.dto.RegistrarClienteBancarioCommand;
import com.reservahoteles.application.dto.RegistrarClienteBancarioResponse;
import com.reservahoteles.application.port.in.RegistrarClienteBancarioUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Pago;
import com.reservahoteles.domain.port.out.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrarClienteBancarioService implements RegistrarClienteBancarioUseCase {

    private final PagoRepository pagoRepo;

    @Override
    public RegistrarClienteBancarioResponse ejecutar(RegistrarClienteBancarioCommand cmd) {
        // 1) Validar permisos
        if (!cmd.idCliente().equals(cmd.idActor()) && cmd.rolActor() != Rol.ADMINISTRADOR) {
            return new RegistrarClienteBancarioResponse(null, "FAILURE", "No autorizado para registrar formas de pago de otro cliente.");
        }

        // 2) Crear entidad (usamos entidad Pago como forma de pago)
        Pago formaPago = new Pago();
        formaPago.setIdCliente(cmd.idCliente());
        formaPago.setTipoPago(cmd.tipoPago());
        formaPago.setNumeroCuenta(cmd.numeroCuenta()); // suponiendo que ya es un sufijo/token
        formaPago.setTitular(cmd.titular());
        formaPago.setExpiracion(cmd.expiracion());
        formaPago.setOtrosDetalles(cmd.otrosDetalles());
        formaPago.setActivo(true);
        formaPago.setFechaRegistro(LocalDateTime.now());
        formaPago.setUltimaActualizacion(LocalDateTime.now());

        // 3) Guardar
        Pago guardado = pagoRepo.save(formaPago);

        return new RegistrarClienteBancarioResponse(guardado.getId(), "SUCCESS", "Método de pago registrado.");
    }
}