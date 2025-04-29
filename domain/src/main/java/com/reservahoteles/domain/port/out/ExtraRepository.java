package com.reservahoteles.domain.port.out;

import com.reservahoteles.domain.entity.Extra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Contrato para la persistencia y consulta de Extras.
 */
public interface ExtraRepository {

    /** Persiste un extra (crea o actualiza). */
    Extra save(Extra extra);

    /** Busca un extra por su id. */
    Optional<Extra> findById(Long id);

    /** Devuelve todos los extras (no paginado). */
    List<Extra> findAll();

    /** Búsqueda por nombre (no paginado). */
    List<Extra> buscarPorNombre(String nombreExtra);

    /** Devuelve todos los extras con esos IDs. */
    List<Extra> findByIds(List<Long> ids);

    /** Elimina un extra por su id. */
    void eliminar(Long id);

    // ───── Métodos paginados ─────

    /** Devuelve todos los extras, paginados. */
    Page<Extra> findAll(Pageable pageable);

    /** Devuelve solo los extras de una habitación (paginado). */
    Page<Extra> findByHabitacionId(Long idHabitacion, Pageable pageable);
}