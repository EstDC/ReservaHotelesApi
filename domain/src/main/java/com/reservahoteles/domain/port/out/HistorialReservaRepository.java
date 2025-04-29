package com.reservahoteles.domain.port.out;

import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.domain.entity.HistorialReserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Contrato para la persistencia y consulta del historial de reservas.
 */
public interface HistorialReservaRepository {

    /**
     * Persiste un historial de reserva (archiva una reserva).
     * @param historial entidad historial de reserva.
     * @return el historial persistido.
     */
    HistorialReserva save(HistorialReserva historial);

    /**
     * Busca un historial por su identificador.
     * @param id identificador.
     * @return un Optional con el historial si se encontró.
     */
    Optional<HistorialReserva> findById(Long id);

    List<HistorialReserva> findAll();

    /**
     * Busca todos los historiales asociados a un cliente específico.
     * @param idCliente identificador del cliente.
     * @return lista de historiales.
     */
    List<HistorialReserva> findByClienteId(Long idCliente);

    Page<HistorialReserva> findByClienteId(Long idCliente, Pageable pageable);
    /**
     * Consulta el historial de reservas aplicado a criterios avanzados.
     * @param idReserva identificador de la reserva (opcional).
     * @param idCliente identificador del cliente (opcional).
     * @param idHabitacion identificador de la habitación (opcional).
     * @param fechaInicio inicio del rango (opcional).
     * @param fechaFin fin del rango (opcional).
     * @param estadoReserva estado de la reserva archivada (opcional).
     * @return lista de registros del historial.
     */
    List<HistorialReserva> buscarPorCriteriosAvanzados(
            Long idReserva,
            Long idCliente,
            Long idHabitacion,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            EstadoReserva estadoReserva
    );

    Page<HistorialReserva> buscarPorCriteriosAvanzados(
            Long idReserva,
            Long idCliente,
            Long idHabitacion,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            EstadoReserva estadoReserva,
            Pageable pageable
    );

    /**
     * Elimina un registro del historial (si la política lo requiere).
     * @param id identificador del registro del historial.
     */
    void eliminar(Long id);
}