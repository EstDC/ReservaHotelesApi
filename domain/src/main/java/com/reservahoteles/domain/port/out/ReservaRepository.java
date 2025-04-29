package com.reservahoteles.domain.port.out;

import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.domain.entity.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Contrato para la persistencia y consulta de reservas.
 */
public interface ReservaRepository {

    /**
     * Persiste una reserva.
     * @param reserva entidad reserva.
     * @return la reserva persistida.
     */
    Reserva save(Reserva reserva);

    /**
     * Busca una reserva por su identificador.
     * @param id identificador.
     * @return un Optional con la reserva si se encontró.
     */
    Optional<Reserva> findById(Long id);

    /**
     * Devuelve todas las reservas.
     * @return lista de reservas.
     */
    List<Reserva> findAll();
    /**
     * Busca reservas según criterios opcionales.
     * Todos los parámetros son opcionales; si vienen como null, se ignoran.
     * @param idCliente identificador del cliente (opcional).
     * @param fechaInicio inicio del rango de fechas (opcional).
     * @param fechaFin fin del rango de fechas (opcional).
     * @param estadoReserva estado de la reserva (opcional).
     * @return lista de reservas que cumplen los criterios.
     */
    List<Reserva> buscarPorCriterios(
            Long idCliente,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            EstadoReserva estadoReserva
    );

    /**
     * Busca reservas aplicando criterios opcionales.
     * @param idCliente identificador del cliente (opcional).
     * @param fechaInicio inicio del rango de fechas (opcional).
     * @param fechaFin fin del rango de fechas (opcional).
     * @param estadoReserva estado de la reserva (opcional).
     * @return lista de reservas que cumplen los criterios.
     */
    List<Reserva> buscarPorCriteriosAvanzados(Long idCliente, Long idHabitacion, LocalDate fechaInicio, LocalDate fechaFin, EstadoReserva estadoReserva);
    Page<Reserva> buscarPorCriteriosAvanzados(
            Long idCliente,
            Long idHabitacion,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            EstadoReserva estadoReserva,
            Pageable pageable
    );
    /**
     * Elimina o desactiva una reserva.
     * @param id identificador de la reserva.
     */
    void eliminar(Long id);
}