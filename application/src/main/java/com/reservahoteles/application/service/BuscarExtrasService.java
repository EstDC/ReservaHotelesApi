package com.reservahoteles.application.service;


import com.PageResponse;
import com.reservahoteles.application.dto.BuscarExtrasCommand;
import com.reservahoteles.application.dto.ExtraResponse;
import com.reservahoteles.application.port.in.BuscarExtrasUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Extra;
import com.reservahoteles.domain.port.out.ExtraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuscarExtrasService implements BuscarExtrasUseCase {

    private final ExtraRepository extraRepo;

    @Override
    public PageResponse<ExtraResponse> ejecutar(BuscarExtrasCommand cmd) {
        if (cmd.idActor() == null || cmd.rolActor() == null) {
            throw new IllegalArgumentException("idActor y rolActor son obligatorios");
        }

        Pageable pageable = PageRequest.of(cmd.page(), cmd.size(), Sort.by("id").ascending());

        // 1) Si piden un idExtra concreto → devuelve sólo ese
        if (cmd.idExtra() != null) {
            Extra e = extraRepo.findById(cmd.idExtra())
                    .orElseThrow(() -> new NoSuchElementException("Extra no encontrado id=" + cmd.idExtra()));
            var dto = new ExtraResponse(e.getId(), e.getNombreExtra(), e.getCostoAdicional());
            return new PageResponse<>(
                    List.of(dto),
                    0, 1, 1, 1
            );
        }

        Page<Extra> page;
        // 2) Admin ve TODO el catálogo
        if (cmd.rolActor() == Rol.ADMINISTRADOR) {
            page = extraRepo.findAll(pageable);

            // 3) Cliente ve sólo extras de su habitación
        } else {
            if (cmd.idHabitacion() == null) {
                throw new IllegalArgumentException("idHabitacion es obligatorio para clientes");
            }
            page = extraRepo.findByHabitacionId(cmd.idHabitacion(), pageable);
        }

        // 4) Mapear a DTO
        List<ExtraResponse> items = page.getContent().stream()
                .map(e -> new ExtraResponse(e.getId(), e.getNombreExtra(), e.getCostoAdicional()))
                .collect(Collectors.toList());

        return new PageResponse<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}