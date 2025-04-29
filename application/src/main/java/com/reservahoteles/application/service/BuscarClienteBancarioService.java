package com.reservahoteles.application.service;

import com.PageResponse;
import com.reservahoteles.application.dto.BuscarClienteBancarioCommand;
import com.reservahoteles.application.dto.ClienteBancarioResponse;
import com.reservahoteles.application.port.in.BuscarClienteBancarioUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Pago;
import com.reservahoteles.domain.port.out.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarClienteBancarioService implements BuscarClienteBancarioUseCase {

    private final PagoRepository pagoRepo;

    @Override
    public PageResponse<ClienteBancarioResponse> ejecutar(BuscarClienteBancarioCommand cmd) {
        // 1) Si piden un idFormaPago concreto → devolvemos ese único
        if (cmd.idFormaPago() != null) {
            Pago p = pagoRepo.findById(cmd.idFormaPago()).orElse(null);
            if (p == null || !p.isActivo()
                    || (!p.getIdCliente().equals(cmd.idActor()) && cmd.rolActor() != Rol.ADMINISTRADOR)
            ) {
                // no existe o no autorizado → página vacía
                return new PageResponse<>(List.of(), 0, 1, 0, 0);
            }
            // mapeo
            var dto = new ClienteBancarioResponse(
                    p.getId(),
                    p.getTipoPago(),
                    p.getNumeroCuenta(),
                    p.getTitular(),
                    p.getExpiracion(),
                    p.getOtrosDetalles(),
                    p.getFechaRegistro(),
                    p.getUltimaActualizacion(),
                    p.isActivo()
            );
            return new PageResponse<>(
                    List.of(dto),
                    /*page*/ 0,
                    /*size*/ 1,
                    /*totalElements*/ 1,
                    /*totalPages*/ 1
            );
        }

        // 2) Si no piden id → listamos paginado
        Pageable pageable = PageRequest.of(
                cmd.page(),
                cmd.size(),
                Sort.by("fechaRegistro").descending()
        );

        Page<Pago> page = (cmd.rolActor() == Rol.ADMINISTRADOR)
                ? pagoRepo.findAllActivas(pageable)
                : pagoRepo.findByIdClienteAndActivoTrue(cmd.idActor(), pageable);

        List<ClienteBancarioResponse> items = page.getContent().stream()
                .map(p -> new ClienteBancarioResponse(
                        p.getId(),
                        p.getTipoPago(),
                        p.getNumeroCuenta(),
                        p.getTitular(),
                        p.getExpiracion(),
                        p.getOtrosDetalles(),
                        p.getFechaRegistro(),
                        p.getUltimaActualizacion(),
                        p.isActivo()
                ))
                .toList();

        return new PageResponse<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}