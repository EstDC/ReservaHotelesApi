package com.reservahoteles.infra.repository;

import com.reservahoteles.infra.entity.ClienteEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringClienteRepository extends JpaRepository<ClienteEntity, Long> {

    List<ClienteEntity> findByEmailContainingIgnoreCase(String email);

    // Métodos derivados para otros criterios
    List<ClienteEntity> findByIdentificacion(String identificacion);

    List<ClienteEntity> findByTelefono(String telefono);

    List<ClienteEntity> findByNombreContainingIgnoreCase(String nombre);

    // Método customizado que combine varios parámetros.
    // Los parámetros se deben evaluar en la consulta, aunque para muchos filtros opcionales se recomienda usar Specifications.
    @Query("SELECT c FROM ClienteEntity c " +
            "WHERE (:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
            "AND (:identificacion IS NULL OR c.identificacion = :identificacion) " +
            "AND (:telefono IS NULL OR c.telefono = :telefono) " +
            "AND (:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))")
    List<ClienteEntity> buscarPorCriterios(@Param("email") String email,
                                           @Param("identificacion") String identificacion,
                                           @Param("telefono") String telefono,
                                           @Param("nombre") String nombre);

    @Query("SELECT c FROM ClienteEntity c " +
            "WHERE (:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
            "  AND (:identificacion IS NULL OR c.identificacion = :identificacion) " +
            "  AND (:telefono IS NULL OR c.telefono = :telefono) " +
            "  AND (:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))")
    Page<ClienteEntity> buscarPorCriterios(
            @Param("email") String email,
            @Param("identificacion") String identificacion,
            @Param("telefono") String telefono,
            @Param("nombre") String nombre,
            Pageable pageable
    );

    // El método para eliminar por id ya lo hereda la interfaz JpaRepository.
    // Método para eliminar un cliente por nombre.
    @Modifying
    @Transactional
    @Query("DELETE FROM ClienteEntity c WHERE c.nombre = :nombre")
    void deleteByNombre(@Param("nombre") String nombre);
}