package com.reservahoteles.domain.port.out;

import com.reservahoteles.domain.entity.Servicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Contrato para la persistencia y consulta de servicios.
 */
public interface ServicioRepository {


    /**
     * Crea o actualiza un servicio.
     */
    Servicio save(Servicio servicio);

    /**
     * Busca un servicio por su id.
     */
    Optional<Servicio> findById(Long id);

    /**
     * Lista todos los servicios, paginados.
     */
    Page<Servicio> findAll(Pageable pageable);

    /**
     * Busca servicios filtrando opcionalmente por nombre o descripción, paginado.
     *
     * @param nombre      texto contenido en el nombre (opcional).
     * @param descripcion texto contenido en la descripción (opcional).
     * @param pageable    paginación y orden.
     */
    Page<Servicio> buscarPorCriterios(String nombre,
                                      String descripcion,
                                      Pageable pageable);

    /**
     * Conveniencia: busca solo por nombre.
     */
    default Page<Servicio> buscarPorNombre(String nombre, Pageable pageable) {
        return buscarPorCriterios(nombre, null, pageable);
    }

    /**
     * Recupera los servicios cuyos identificadores estén en la lista recibida.
     */
    List<Servicio> findByIds(List<Long> ids);

    /**
     * Elimina un servicio por su id.
     */
    void eliminar(Long id);
}