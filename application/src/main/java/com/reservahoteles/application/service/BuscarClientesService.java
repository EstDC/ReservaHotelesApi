package com.reservahoteles.application.service;

import com.PageResponse;
import com.reservahoteles.application.dto.BuscarClientesCommand;
import com.reservahoteles.application.port.in.BuscarClientesUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Cliente;
import com.reservahoteles.domain.port.out.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import com.reservahoteles.application.dto.BuscarClientesResponse;


@Service
@RequiredArgsConstructor
public class BuscarClientesService implements BuscarClientesUseCase {

    private final ClienteRepository clienteRepo;

    @Override
    public PageResponse<BuscarClientesResponse> ejecutar(BuscarClientesCommand cmd) {
        boolean esAdmin = cmd.rolActor() == Rol.ADMINISTRADOR;

        // 1) Si no es admin → sólo perfil propio
        if (!esAdmin) {
            Cliente self = clienteRepo.findById(cmd.idActor())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Cliente no encontrado: id=" + cmd.idActor()
                    ));
            BuscarClientesResponse dto = toResponse(self);
            return new PageResponse<>(
                    List.of(dto),
                    /*page=*/0, /*size=*/1,
                    /*totalElements=*/1, /*totalPages=*/1
            );
        }

        // 2) Si admin pide un ID concreto
        if (cmd.id() != null) {
            Cliente c = clienteRepo.findById(cmd.id())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Cliente no encontrado: id=" + cmd.id()
                    ));
            BuscarClientesResponse dto = toResponse(c);
            return new PageResponse<>(
                    List.of(dto),
                    0, 1, 1, 1
            );
        }

        // 3) Build pageable
        Pageable pageable = PageRequest.of(
                cmd.page(),
                cmd.size(),
                Sort.by("id").ascending()
        );

        // 4) Detectar si hay filtros “básicos”
        boolean sinFiltrosBase =
                isBlank(cmd.email()) &&
                        isBlank(cmd.identificacion()) &&
                        isBlank(cmd.telefono()) &&
                        isBlank(cmd.nombre());

        Page<Cliente> page;
        if (sinFiltrosBase) {
            // findAll paginado
            page = clienteRepo.findAll(pageable);
        } else {
            // query derivada + paginado
            page = clienteRepo.buscarPorCriterios(
                    nullToBlank(cmd.email()),
                    nullToBlank(cmd.identificacion()),
                    nullToBlank(cmd.telefono()),
                    nullToBlank(cmd.nombre()),
                    pageable
            );
        }

        // 5) Filtrado “extra” en memoria: dirección y tipoIdentificacion
        List<BuscarClientesResponse> items = page.getContent().stream()
                .filter(c -> contains(c.getDireccion(), cmd.direccion()))
                .filter(c -> equalsOrNull(c.getTipoIdentificacion(), cmd.tipoIdentificacion()))
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    private BuscarClientesResponse toResponse(Cliente c) {
        return new BuscarClientesResponse(
                c.getId(),
                c.getNombre(),
                c.getEmail(),
                c.getTelefono(),
                c.getDireccion(),
                c.getTipoIdentificacion(),
                c.getIdentificacion(),
                c.getFechaRegistro(),
                c.getUltimaActualizacion(),
                c.getRol()
        );
    }

    // — utilitarios para filtros —
    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
    private String nullToBlank(String s) {
        return isBlank(s) ? null : s.trim();
    }
    private boolean contains(String valor, String criterio) {
        if (isBlank(criterio)) return true;
        return valor != null && valor.toLowerCase().contains(criterio.trim().toLowerCase());
    }
    private <E> boolean equalsOrNull(E valor, E criterio) {
        return criterio == null || criterio.equals(valor);
    }
}
