package com.reservahoteles.application.service;

import com.PageResponse;
import com.reservahoteles.application.dto.ConsultarPagosCommand;
import com.reservahoteles.application.dto.PagoResponse;
import com.reservahoteles.application.port.in.ConsultarPagosUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Pago;
import com.reservahoteles.domain.port.out.PagoRepository;
import com.reservahoteles.domain.port.out.ReservaRepository;
import com.reservahoteles.domain.port.out.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultarPagosService implements ConsultarPagosUseCase {

    private final PagoRepository    pagoRepo;
    private final ReservaRepository reservaRepo;
    private final ClienteRepository clienteRepo;

    @Override
    public PageResponse<PagoResponse> ejecutar(ConsultarPagosCommand cmd) {
        // 1) Validación rango de fechas
        if (cmd.fechaInicio() != null && cmd.fechaFin() != null
                && cmd.fechaInicio().isAfter(cmd.fechaFin())) {
            throw new IllegalArgumentException(
                    "Rango de fechas inválido: fechaInicio > fechaFin");
        }

        // 2) Seguridad: sólo admin puede ver pagos de otros clientes
        boolean esAdmin  = cmd.rolActor() == Rol.ADMINISTRADOR;
        if (!esAdmin) {
            if (cmd.idCliente() != null
                    && !Objects.equals(cmd.idCliente(), cmd.idActor())) {
                throw new IllegalStateException("No tienes permiso para ver los pagos de otro cliente");
            }
        }

        // 3) Si pide un pago por ID, lo devolvemos directamente (si está permitido)
        if (cmd.idPago() != null) {
            Pago p = pagoRepo.findById(cmd.idPago())
                    .orElseThrow(() -> new NoSuchElementException(
                            "No existe pago con id = " + cmd.idPago()
                    ));
            if (!esAdmin && !Objects.equals(p.getIdCliente(), cmd.idActor())) {
                throw new IllegalStateException("No tienes permiso para ver este pago");
            }
            PagoResponse dto = mapToDto(p);
            // devolver un PageResponse con un solo elemento
            return new PageResponse<>(
                    List.of(dto),
                    /*page=*/ 0,
                    /*size=*/ 1,
                    /*totalElements=*/ 1,
                    /*totalPages=*/ 1
            );
        }

        // 4) Validar existencia de filtros relacionados
        if (cmd.idReserva() != null) {
            reservaRepo.findById(cmd.idReserva())
                    .orElseThrow(() -> new NoSuchElementException(
                            "No existe reserva con id = " + cmd.idReserva()
                    ));
        }
        if (esAdmin && cmd.idCliente() != null) {
            clienteRepo.findById(cmd.idCliente())
                    .orElseThrow(() -> new NoSuchElementException(
                            "No existe cliente con id = " + cmd.idCliente()
                    ));
        }

        // 5) Ajustar filtro de cliente para no-admin
        Long filtroCliente = esAdmin
                ? cmd.idCliente()
                : cmd.idActor();

        // 6) Crear el Pageable ───────────────────────────
        Pageable pageable = PageRequest.of(cmd.page(), cmd.size(), Sort.by("fechaPago").descending());

        // 7) Llamada paginada al repositorio con criterios
        Page<Pago> page = pagoRepo.buscarPorCriterios(
                cmd.idReserva(),
                filtroCliente,
                cmd.fechaInicio(),
                cmd.fechaFin(),
                cmd.estadoPago(),
                pageable
        );

        // 8) Mapear el contenido a DTOs
        List<PagoResponse> items = page.getContent().stream()
                .map(this::mapToDto)
                .toList();

        // 9) Devolver PageResponse con datos y metadatos de paginación
        return new PageResponse<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    private PagoResponse mapToDto(Pago p) {
        return new PagoResponse(
                p.getId(),
                p.getIdReserva(),
                p.getIdCliente(),
                p.getEstadoPago(),
                p.getFechaPago(),
                p.getMonto(),
                p.getUltimaActualizacion()
        );
    }
}