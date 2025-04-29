package com.reservahoteles.infra.repository;

import com.reservahoteles.common.enums.EstadoHabitacion;
import com.reservahoteles.common.enums.TipoHabitacion;
import com.reservahoteles.infra.entity.HabitacionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SpringHabitacionRepository
        extends JpaRepository<HabitacionEntity, Long> {

    /* ──────── consultas “simples” ──────── */
    // Versión no paginada (por compatibilidad):
    List<HabitacionEntity> findByIdHotel(Long idHotel);
    // Versión paginada:
    Page<HabitacionEntity> findByIdHotel(Long idHotel, Pageable pageable);

    List<HabitacionEntity> findByIdHotelAndEstado(Long idHotel, EstadoHabitacion estado);
    Page<HabitacionEntity> findByIdHotelAndEstado(Long idHotel, EstadoHabitacion estado, Pageable pageable);

    List<HabitacionEntity> findByIdHotelAndTipo(Long idHotel, TipoHabitacion tipo);
    Page<HabitacionEntity> findByIdHotelAndTipo(Long idHotel, TipoHabitacion tipo, Pageable pageable);

    /* ── filtros opcionales ── */
    @Query("""
        SELECT h
          FROM HabitacionEntity h
         WHERE (:idHotel IS NULL OR h.idHotel          = :idHotel)
           AND (:numeroHabitacion IS NULL OR h.numeroHabitacion = :numeroHabitacion)
           AND (:capacidad IS NULL OR h.capacidad        = :capacidad)
           AND (:precioMin IS NULL OR h.precioPorNoche  >= :precioMin)
           AND (:estado    IS NULL OR h.estado           = :estado)
    """)
    List<HabitacionEntity> buscarPorCriterios(
            @Param("idHotel")          Long idHotel,
            @Param("numeroHabitacion") String numeroHabitacion,
            @Param("capacidad")        Long capacidad,
            @Param("precioMin")        BigDecimal precioMin,
            @Param("estado")           EstadoHabitacion estado
    );

    /* ── Ahora la sobrecarga paginada ── */
    @Query("""
        SELECT h
          FROM HabitacionEntity h
         WHERE (:idHotel IS NULL OR h.idHotel          = :idHotel)
           AND (:numeroHabitacion IS NULL OR h.numeroHabitacion = :numeroHabitacion)
           AND (:capacidad IS NULL OR h.capacidad        = :capacidad)
           AND (:precioMin IS NULL OR h.precioPorNoche  >= :precioMin)
           AND (:estado    IS NULL OR h.estado           = :estado)
    """)
    Page<HabitacionEntity> buscarPorCriterios(
            @Param("idHotel")          Long idHotel,
            @Param("numeroHabitacion") String numeroHabitacion,
            @Param("capacidad")        Long capacidad,
            @Param("precioMin")        BigDecimal precioMin,
            @Param("estado")           EstadoHabitacion estado,
            Pageable pageable
    );


    /* ── Mismo para avanzado ── */
    @Query("""
        SELECT h
          FROM HabitacionEntity h
         WHERE h.idHotel = :idHotel
           AND (:estado     IS NULL OR h.estado    = :estado)
           AND (:tipo       IS NULL OR h.tipo      = :tipo)
           AND (:capMin     IS NULL OR h.capacidad >= :capMin)
           AND (:capMax     IS NULL OR h.capacidad <= :capMax)
           AND (:precioMin  IS NULL OR h.precioPorNoche >= :precioMin)
           AND (:precioMax  IS NULL OR h.precioPorNoche <= :precioMax)
    """)
    List<HabitacionEntity> buscarPorCriteriosAvanzados(
            @Param("idHotel")   Long idHotel,
            @Param("estado")    EstadoHabitacion estado,
            @Param("tipo")      TipoHabitacion tipo,
            @Param("capMin")    Long capacidadMin,
            @Param("capMax")    Long capacidadMax,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax
    );

    @Query("""
        SELECT h
          FROM HabitacionEntity h
         WHERE h.idHotel = :idHotel
           AND (:estado     IS NULL OR h.estado    = :estado)
           AND (:tipo       IS NULL OR h.tipo      = :tipo)
           AND (:capMin     IS NULL OR h.capacidad >= :capMin)
           AND (:capMax     IS NULL OR h.capacidad <= :capMax)
           AND (:precioMin  IS NULL OR h.precioPorNoche >= :precioMin)
           AND (:precioMax  IS NULL OR h.precioPorNoche <= :precioMax)
    """)
    Page<HabitacionEntity> buscarPorCriteriosAvanzados(
            @Param("idHotel")   Long idHotel,
            @Param("estado")    EstadoHabitacion estado,
            @Param("tipo")      TipoHabitacion tipo,
            @Param("capMin")    Long capacidadMin,
            @Param("capMax")    Long capacidadMax,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax,
            Pageable pageable
    );
}