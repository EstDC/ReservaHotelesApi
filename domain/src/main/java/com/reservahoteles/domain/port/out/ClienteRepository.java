package com.reservahoteles.domain.port.out;


import com.reservahoteles.domain.entity.Cliente;

import java.util.List;
import java.util.Optional;

import com.reservahoteles.domain.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Contrato para la persistencia y consulta de clientes.
 */
public interface ClienteRepository {

    /**
     * Persiste un cliente (crea o actualiza).
     * @param cliente entidad cliente.
     * @return el cliente persistido.
     */
    Cliente save(Cliente cliente);

    /**
     * Busca un cliente por su identificador.
     * @param id identificador del cliente.
     * @return Un Optional con el cliente si se encontró.
     */
    Optional<Cliente> findById(Long id);

    /**
     * Devuelve todos los clientes registrados.
     * @return lista de clientes.
     */
    List<Cliente> findAll();

    /**
     * Sobrecarga paginada para listar todos los clientes.
     */
    Page<Cliente> findAll(Pageable pageable);

    /**
     * Busca clientes según criterios opcionales.
     * Todos los parámetros son opcionales; si vienen como null, se ignoran.
     * @param email correo electrónico.
     * @param identificacion identificación (ej. DNI).
     * @param telefono teléfono.
     * @param nombre nombre o parte del nombre.
     * @return lista de clientes que cumplan los criterios.
     */
    List<Cliente> buscarPorCriterios(String email, String identificacion, String telefono, String nombre);

    /**
     * Sobrecarga paginada para buscar según criterios opcionales.
     */
    Page<Cliente> buscarPorCriterios(
            String email,
            String identificacion,
            String telefono,
            String nombre,
            Pageable pageable
    );

    /**
     * Elimina o desactiva un cliente según la política de negocio.
     * @param id identificador del cliente.
     */
    void eliminar(Long id);

    /**
     * Elimina o desactiva clientes por nombre, si se requiere.
     * @param nombre nombre del cliente.
     */
     void eliminarPorNombre(String nombre);
}