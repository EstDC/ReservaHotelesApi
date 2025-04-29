package com.reservahoteles.application.service;

import com.reservahoteles.application.dto.EliminarClienteBancarioCommand;
import com.reservahoteles.application.dto.EliminarClienteBancarioResponse;
import com.reservahoteles.application.port.in.EliminarClienteBancarioUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.port.out.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EliminarClienteBancarioService implements EliminarClienteBancarioUseCase {

    private final PagoRepository pagoRepo;

    @Override
    public EliminarClienteBancarioResponse ejecutar(EliminarClienteBancarioCommand cmd) {
        var formaPago = pagoRepo.findById(cmd.idFormaPago()).orElse(null);

        if (formaPago == null || !formaPago.isActivo()) {
            return new EliminarClienteBancarioResponse("FAILURE", "Método de pago no encontrado o ya inactivo.");
        }

        if (!formaPago.getIdCliente().equals(cmd.idActor()) && cmd.rolActor() != Rol.ADMINISTRADOR) {
            return new EliminarClienteBancarioResponse("FAILURE", "No autorizado para eliminar este método de pago.");
        }

        formaPago.setActivo(false);
        formaPago.setUltimaActualizacion(LocalDateTime.now());
        pagoRepo.save(formaPago);

        return new EliminarClienteBancarioResponse("SUCCESS", "Método de pago eliminado.");
    }
}