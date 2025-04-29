package com.reservahoteles.infra.repository;

import com.reservahoteles.infra.entity.ServicioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringServicioRepository extends JpaRepository<ServicioEntity, Long> {

    List<ServicioEntity> findByNombreContainingIgnoreCase(String nombre);
    List<ServicioEntity> findByDescripcionContainingIgnoreCase(String descripcion);

    @Query("""
      SELECT s
        FROM ServicioEntity s
       WHERE (:nombre      IS NULL OR LOWER(s.nombre)      LIKE LOWER(CONCAT('%',:nombre,'%')))
         AND (:descripcion IS NULL OR LOWER(s.descripcion) LIKE LOWER(CONCAT('%',:descripcion,'%')))
      """)
    List<ServicioEntity> buscarPorCriterios(
            @Param("nombre") String nombre,
            @Param("descripcion") String descripcion
    );

    @Query("SELECT s FROM ServicioEntity s "
            + "WHERE (:nombre IS NULL OR LOWER(s.nombre) LIKE %:nombre%) "
            + "  AND (:descripcion IS NULL OR LOWER(s.descripcion) LIKE %:descripcion%)")
    Page<ServicioEntity> buscarPorCriterios(
            @Param("nombre") String nombre,
            @Param("descripcion") String descripcion,
            Pageable pageable);

    List<ServicioEntity> findAllByIdIn(List<Long> ids);
}