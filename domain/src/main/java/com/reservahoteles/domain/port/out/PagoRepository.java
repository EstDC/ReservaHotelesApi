package com.reservahoteles.domain.port.out;

import com.reservahoteles.common.enums.EstadoPago;
import com.reservahoteles.domain.entity.Pago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Contrato para la persistencia y consulta de pagos.
 */
public interface PagoRepository {

    /**
     * Persiste un pago.
     * @param pago entidad pago.
     * @return el pago persistido.
     */
    Pago save(Pago pago);

    /**
     * Busca un pago por su identificador.
     * @param id identificador.
     * @return un Optional con el pago si se encontró.
     */
    Optional<Pago> findById(Long id);

    /**
     * Devuelve todos los pagos.
     * @return lista de pagos.
     */
    List<Pago> findAll();

    /**
     * Lista paginada de pagos activos.
     * @param pageable parámetros de paginación.
     * @return página de pagos activos.
     */
    Page<Pago> findAllActivas(Pageable pageable);

    /**
     * Busca pagos aplicando criterios opcionales.
     * @param idReserva identificador de la reserva (opcional).
     * @param idCliente identificador del cliente (opcional).
     * @param fechaInicio inicio del rango de fecha (opcional).
     * @param fechaFin fin del rango de fecha (opcional).
     * @param estadoPago estado del pago (opcional).
     * @return lista de pagos que cumplen los criterios.
     */
    List<Pago> buscarPorCriterios(Long idReserva, Long idCliente, LocalDateTime fechaInicio, LocalDateTime fechaFin, EstadoPago estadoPago);
    Page<Pago> buscarPorCriterios(
            Long idReserva,
            Long idCliente,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            EstadoPago estadoPago,
            Pageable pageable
    );
    /**
     * Elimina un pago (si la lógica lo requiere, o implementa soft delete).
     * @param id identificador del pago.
     */
    void eliminar(Long id);

    Page<Pago> findByActivoTrue(Pageable pageable);

    /** Lista no paginada de pagos activos de un cliente */
    List<Pago> findByIdClienteAndActivoTrue(Long idCliente);

    /** Lista paginada de pagos activos de un cliente */
    Page<Pago> findByIdClienteAndActivoTrue(Long idCliente, Pageable pageable);

    List<Pago> findAllActivas();
}