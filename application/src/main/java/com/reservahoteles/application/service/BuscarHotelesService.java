package com.reservahoteles.application.service;
import com.PageResponse;
import com.reservahoteles.application.dto.BuscarHotelesCommand;
import com.reservahoteles.application.dto.ListarHotelesResponse;
import com.reservahoteles.application.port.in.BuscarHotelesUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Hotel;
import com.reservahoteles.domain.port.out.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuscarHotelesService implements BuscarHotelesUseCase {

    private final HotelRepository hotelRepo;

    @Override
    public PageResponse<ListarHotelesResponse> ejecutar(BuscarHotelesCommand cmd) {
        // 1) Control de actor/rol
        if (cmd.idActor() == null || cmd.rolActor() == null) {
            throw new IllegalArgumentException("Debe proporcionarse idActor y rolActor");
        }
        if (cmd.rolActor() != Rol.ADMINISTRADOR && cmd.rolActor() != Rol.CLIENTE) {
            throw new IllegalStateException("Rol no permitido para consultar hoteles");
        }

        // 2) Pageable con orden descendente por fechaRegistro
        Pageable pageable = PageRequest.of(cmd.page(), cmd.size(),
                Sort.by("fechaRegistro").descending());

        // 3) Llamada al repositorio usando **todas** las opciones de filtro
        Page<Hotel> page = hotelRepo.buscarPorCriteriosAvanzados(
                cmd.pais(),
                cmd.ciudad(),
                cmd.numeroEstrellas(),
                cmd.nombreParcial(),
                cmd.direccionParcial(),
                cmd.telefonoParcial(),
                cmd.emailParcial(),
                pageable
        );

        // 4) Mapear dominio → DTO
        List<ListarHotelesResponse> items = page.getContent().stream()
                .map(h -> new ListarHotelesResponse(
                        h.getId(),
                        h.getNombre(),
                        h.getDireccion(),
                        h.getPais(),
                        h.getCiudad(),
                        h.getLatitud(),
                        h.getLongitud(),
                        h.getNumeroEstrellas(),
                        h.getTelefono(),
                        h.getEmail(),
                        h.getFechaRegistro(),
                        h.getUltimaActualizacion()
                ))
                .collect(Collectors.toList());

        // 5) Devolver con metadatos de paginación
        return new PageResponse<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}