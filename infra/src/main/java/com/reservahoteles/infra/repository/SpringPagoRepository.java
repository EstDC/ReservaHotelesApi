package com.reservahoteles.infra.repository;

import com.reservahoteles.common.enums.EstadoPago;
import com.reservahoteles.infra.entity.PagoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SpringPagoRepository extends JpaRepository<PagoEntity, Long> {

    @Query("SELECT p FROM PagoEntity p " +
            "WHERE (:idReserva  IS NULL OR p.idReserva  = :idReserva) " +
            "  AND (:idCliente  IS NULL OR p.idCliente  = :idCliente) " +
            "  AND (:fechaInicio IS NULL OR p.fechaPago >= :fechaInicio) " +
            "  AND (:fechaFin   IS NULL OR p.fechaPago <= :fechaFin) " +
            "  AND (:estadoPago IS NULL OR p.estadoPago = :estadoPago)")
    List<PagoEntity> buscarPorCriterios(
            @Param("idReserva")   Long idReserva,
            @Param("idCliente")   Long idCliente,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin")    LocalDateTime fechaFin,
            @Param("estadoPago")  EstadoPago estadoPago
    );

    @Query("SELECT p FROM PagoEntity p " +
            "WHERE (:idReserva  IS NULL OR p.idReserva  = :idReserva) " +
            "  AND (:idCliente  IS NULL OR p.idCliente  = :idCliente) " +
            "  AND (:fechaInicio IS NULL OR p.fechaPago >= :fechaInicio) " +
            "  AND (:fechaFin   IS NULL OR p.fechaPago <= :fechaFin) " +
            "  AND (:estadoPago IS NULL OR p.estadoPago = :estadoPago)")
    Page<PagoEntity> buscarPorCriterios(
            @Param("idReserva")   Long idReserva,
            @Param("idCliente")   Long idCliente,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin")    LocalDateTime fechaFin,
            @Param("estadoPago")  EstadoPago estadoPago,
            Pageable pageable
    );

    // ── pagos activos ──

    /** No paginado */
    List<PagoEntity> findAllByActivoTrue();

    /** Paginado */
    Page<PagoEntity> findAllByActivoTrue(Pageable pageable);

    // ── pagos activos de un cliente ──

    /** No paginado */
    List<PagoEntity> findByIdClienteAndActivoTrue(Long idCliente);

    /** Paginado */
    Page<PagoEntity> findByIdClienteAndActivoTrue(Long idCliente, Pageable pageable);
}
