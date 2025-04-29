package com.reservahoteles.domain.port.out;

import com.reservahoteles.common.enums.EstadoHabitacion;
import com.reservahoteles.common.enums.TipoHabitacion;
import com.reservahoteles.domain.entity.Habitacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Contrato para la persistencia y consulta de habitaciones.
 */

public interface HabitacionRepository {

    // CRUD básico
    Habitacion save(Habitacion habitacion);
    Optional<Habitacion> findById(Long id);
    List<Habitacion> findAll();
    void eliminar(Long id);

    // ─── Métodos paginados ────────────────────────────────────────────

    /**
     * Búsqueda genérica por hotel + filtros opcionales, paginada.
     */
    Page<Habitacion> buscarPorHotel(
            Long idHotel,
            EstadoHabitacion estado,
            TipoHabitacion tipo,
            Pageable pageable
    );

    /**
     * Búsqueda simple por hotel (sin filtros), paginada.
     */
    Page<Habitacion> findByIdHotel(Long idHotel, Pageable pageable);

    /**
     * Búsqueda por hotel y estado, paginada.
     */
    Page<Habitacion> findByIdHotelAndEstado(
            Long idHotel,
            EstadoHabitacion estado,
            Pageable pageable
    );

    /**
     * Búsqueda por hotel y tipo, paginada.
     */
    Page<Habitacion> findByIdHotelAndTipo(
            Long idHotel,
            TipoHabitacion tipo,
            Pageable pageable
    );

    /**
     * Búsqueda resumida con un subconjunto de filtros opcionales, paginada.
     */
    Page<Habitacion> buscarPorCriterios(
            Long idHotel,
            String numeroHabitacion,
            Long capacidad,
            BigDecimal precioMin,
            EstadoHabitacion estado,
            Pageable pageable
    );

    /**
     * Consulta avanzada con todos los filtros opcionales, paginada.
     */
    Page<Habitacion> buscarPorCriteriosAvanzados(
            Long idHotel,
            EstadoHabitacion estado,
            TipoHabitacion tipo,
            Long capacidadMin,
            Long capacidadMax,
            BigDecimal precioMin,
            BigDecimal precioMax,
            Pageable pageable
    );

    // ─── Métodos no paginados ────────────────────────────────────────

    List<Habitacion> findByIdHotel(Long idHotel);
    List<Habitacion> findByIdHotelAndEstado(Long idHotel, EstadoHabitacion estado);
    List<Habitacion> findByIdHotelAndTipo(Long idHotel, TipoHabitacion tipo);
    List<Habitacion> buscarPorCriterios(
            Long idHotel,
            String numeroHabitacion,
            Long capacidad,
            BigDecimal precioMin,
            EstadoHabitacion estado
    );
    List<Habitacion> buscarPorCriteriosAvanzados(
            Long idHotel,
            EstadoHabitacion estado,
            TipoHabitacion tipo,
            Long capacidadMin,
            Long capacidadMax,
            BigDecimal precioMin,
            BigDecimal precioMax
    );
}