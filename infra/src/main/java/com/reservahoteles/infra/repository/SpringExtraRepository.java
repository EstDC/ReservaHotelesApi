package com.reservahoteles.infra.repository;

import com.reservahoteles.infra.entity.ExtraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface SpringExtraRepository extends JpaRepository<ExtraEntity, Long> {

    /**
     * Consulta derivada para buscar por nombre (contiene, ignore case).
     */
    List<ExtraEntity> findByNombreExtraContainingIgnoreCase(String nombreExtra);

    List<ExtraEntity> findByIdIn(List<Long> ids);

    @Query("SELECT DISTINCT e FROM ExtraEntity e JOIN e.habitaciones h WHERE h.id = :habitacionId")
    Page<ExtraEntity> findByHabitacionId(@Param("habitacionId") Long habitacionId, Pageable pageable);
}