package com.reservahoteles.infra.repository;

import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.infra.entity.HistorialReservaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SpringHistorialReservaRepository extends JpaRepository<HistorialReservaEntity, Long> {

    @Query("SELECT h FROM HistorialReservaEntity h " +
            "WHERE (:idCliente IS NULL OR h.idCliente = :idCliente) " +
            "AND (:fechaInicio IS NULL OR h.fechaEntrada >= :fechaInicio) " +
            "AND (:fechaFin IS NULL OR h.fechaSalida <= :fechaFin) " +
            "AND (:estadoReserva IS NULL OR h.estadoReserva = :estadoReserva)")
    List<HistorialReservaEntity> buscarPorCriterios(@Param("idCliente") Long idCliente,
                                                    @Param("fechaInicio") LocalDate fechaInicio,
                                                    @Param("fechaFin") LocalDate fechaFin,
                                                    @Param("estadoReserva") EstadoReserva estadoReserva);


    List<HistorialReservaEntity> findByIdCliente(Long idCliente);
    Page<HistorialReservaEntity> findByIdCliente(Long idCliente, Pageable pageable);


    // Búsqueda avanzada
    @Query("""
        SELECT h
          FROM HistorialReservaEntity h
         WHERE (:idReserva     IS NULL OR h.idReserva     = :idReserva)
           AND (:idCliente     IS NULL OR h.idCliente     = :idCliente)
           AND (:idHabitacion  IS NULL OR h.idHabitacion  = :idHabitacion)
           AND (:fechaInicio   IS NULL OR h.fechaArchivo >= :fechaInicio)
           AND (:fechaFin      IS NULL OR h.fechaArchivo <= :fechaFin)
           AND (:estadoReserva IS NULL OR h.estadoReserva = :estadoReserva)
    """)
    List<HistorialReservaEntity> buscarPorCriteriosAvanzados(
            @Param("idReserva") Long idReserva,
            @Param("idCliente") Long idCliente,
            @Param("idHabitacion") Long idHabitacion,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("estadoReserva") EstadoReserva estadoReserva
    );

    /** Paginado de buscarPorCriteriosAvanzados */
    @Query("""
        SELECT h
          FROM HistorialReservaEntity h
         WHERE (:idReserva     IS NULL OR h.idReserva     = :idReserva)
           AND (:idCliente     IS NULL OR h.idCliente     = :idCliente)
           AND (:idHabitacion  IS NULL OR h.idHabitacion  = :idHabitacion)
           AND (:fechaInicio   IS NULL OR h.fechaArchivo >= :fechaInicio)
           AND (:fechaFin      IS NULL OR h.fechaArchivo <= :fechaFin)
           AND (:estadoReserva IS NULL OR h.estadoReserva = :estadoReserva)
    """)
    Page<HistorialReservaEntity> buscarPorCriteriosAvanzados(
            @Param("idReserva")     Long idReserva,
            @Param("idCliente")     Long idCliente,
            @Param("idHabitacion")  Long idHabitacion,
            @Param("fechaInicio")   LocalDate fechaInicio,
            @Param("fechaFin")      LocalDate fechaFin,
            @Param("estadoReserva") EstadoReserva estadoReserva,
            Pageable pageable
    );
}