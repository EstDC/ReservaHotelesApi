package com.reservahoteles.domain.port.out;

import com.reservahoteles.domain.entity.Credencial;

import java.util.Optional;

/**
 * Contrato para la persistencia y consulta de credenciales de usuario.
 */
public interface CredencialRepository {

    /**
     * Persiste una credencial (crea o actualiza).
     */
    Credencial save(Credencial credencial);

    /**
     * Busca una credencial a partir de su propio identificador.
     */
    Optional<Credencial> findById(Long id);

    /**
     * Busca la credencial asociada a un cliente dado.
     */
    Optional<Credencial> findByClienteId(Long clienteId);

    /**
     * Busca una credencial por nombre de usuario.
     */
    Optional<Credencial> findByUsername(String username);
    /**
     * Busca una credencial por su token de recuperación de contraseña.
     */
    Optional<Credencial> findByResetToken(String token);

    /**
     * Elimina una credencial por su identificador.
     */
    void eliminar(Long id);

    /**
     * Elimina la credencial asociada a un cliente.
     */
    void eliminarPorClienteId(Long clienteId);
}