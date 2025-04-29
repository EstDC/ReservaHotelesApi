package com.reservahoteles.infra.repository;

import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.infra.entity.ReservaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SpringReservaRepository extends JpaRepository<ReservaEntity, Long> {

    /**
     * Búsqueda “simple” por cliente, fecha y estado.
     */
    @Query("""
           SELECT r
             FROM ReservaEntity r
            WHERE (:idCliente     IS NULL OR r.idCliente     = :idCliente)
              AND (:fechaInicio   IS NULL OR r.fechaEntrada  >= :fechaInicio)
              AND (:fechaFin      IS NULL OR r.fechaSalida   <= :fechaFin)
              AND (:estadoReserva IS NULL OR r.estadoReserva = :estadoReserva)
           """)
    List<ReservaEntity> buscarPorCriterios(
            @Param("idCliente")     Long idCliente,
            @Param("fechaInicio")   LocalDate fechaInicio,
            @Param("fechaFin")      LocalDate fechaFin,
            @Param("estadoReserva") EstadoReserva estadoReserva
    );


    // Consulta personalizada para búsquedas avanzadas. Se pueden incluir filtros opcionales:
    @Query("SELECT r FROM ReservaEntity r " +
            "WHERE (:idCliente IS NULL OR r.idCliente = :idCliente) " +
            "AND (:idHabitacion IS NULL OR r.idHabitacion = :idHabitacion) " +
            "AND (:fechaInicio IS NULL OR r.fechaEntrada >= :fechaInicio) " +
            "AND (:fechaFin IS NULL OR r.fechaSalida <= :fechaFin) " +
            "AND (:estadoReserva IS NULL OR r.estadoReserva = :estadoReserva)")
    List<ReservaEntity> buscarPorCriteriosAvanzados(@Param("idCliente") Long idCliente,
                                                    @Param("idHabitacion") Long idHabitacion,
                                                    @Param("fechaInicio") LocalDate fechaInicio,
                                                    @Param("fechaFin") LocalDate fechaFin,
                                                    @Param("estadoReserva") EstadoReserva estadoReserva);

    @Query("""
      SELECT r FROM ReservaEntity r
       WHERE r.idCliente       = :idCliente
         AND (:idHabitacion IS NULL OR r.idHabitacion = :idHabitacion)
         AND (:fechaInicio  IS NULL OR r.fechaReserva >= :fechaInicio)
         AND (:fechaFin     IS NULL OR r.fechaReserva <= :fechaFin)
         AND (:estadoReserva IS NULL OR r.estadoReserva = :estadoReserva)
    """)
        Page<ReservaEntity> buscarPorCriteriosAvanzados(
                @Param("idCliente")       Long idCliente,
                @Param("idHabitacion")    Long idHabitacion,
                @Param("fechaInicio")     LocalDate fechaInicio,
                @Param("fechaFin")        LocalDate fechaFin,
                @Param("estadoReserva")   EstadoReserva estadoReserva,
                Pageable pageable
        );
}