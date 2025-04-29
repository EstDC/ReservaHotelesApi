package com.reservahoteles.application.service;

import com.PageResponse;
import com.reservahoteles.application.dto.ConsultarHabitacionesCommand;
import com.reservahoteles.application.dto.HabitacionResponse;
import com.reservahoteles.application.port.in.ConsultarHabitacionesPorHotelUseCase;
import com.reservahoteles.common.enums.EstadoHabitacion;
import com.reservahoteles.common.enums.TipoHabitacion;
import com.reservahoteles.domain.entity.Habitacion;
import com.reservahoteles.domain.port.out.HabitacionRepository;
import com.reservahoteles.domain.port.out.HotelRepository;
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
public class ConsultarHabitacionesPorHotelService implements ConsultarHabitacionesPorHotelUseCase {

    private final HotelRepository hotelRepo;
    private final HabitacionRepository habitacionRepo;

    @Override
    public PageResponse<HabitacionResponse> ejecutar(ConsultarHabitacionesCommand cmd) {
        // 1) idHotel es obligatorio
        if (cmd.idHotel() == null) {
            throw new IllegalArgumentException("El id del hotel es obligatorio");
        }

        // 2) Verificar que el hotel exista
        hotelRepo.findById(cmd.idHotel())
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró el hotel con id = " + cmd.idHotel()
                ));

        // 3) Sacar filtros directamente del comando (pueden ser null)
        EstadoHabitacion estadoFilter = cmd.estado();
        TipoHabitacion tipoFilter = cmd.tipo();

        // 4) Construir el Pageable (página, tamaño y opcionalmente orden)
        Pageable pageable = PageRequest.of(
                cmd.page(),
                cmd.size(),
                Sort.by("numeroHabitacion").ascending()
        );

        // 5) Consultar el repositorio paginado
        Page<Habitacion> page = habitacionRepo.buscarPorHotel(
                cmd.idHotel(),
                estadoFilter,
                tipoFilter,
                pageable
        );

        // 6) Mapear el contenido de la página a tu DTO de respuesta
        List<HabitacionResponse> items = page.getContent().stream()
                .map(h -> new HabitacionResponse(
                        h.getId(),
                        h.getNumeroHabitacion(),
                        h.getTipo(),
                        h.getCapacidad(),
                        h.getPrecioPorNoche(),
                        h.getEstado(),
                        h.getDescripcion()
                ))
                .toList();

        // 7) Devolver PageResponse con la lista y metadatos de paginación
        return new PageResponse<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}