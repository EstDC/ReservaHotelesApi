package com.reservahoteles.infra.repository;

import com.reservahoteles.infra.entity.CredencialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SpringCredencialRepository extends JpaRepository<CredencialEntity, Long> {
    Optional<CredencialEntity> findByUsername(String username);

    Optional<CredencialEntity> findByIdCliente(Long idCliente);

    Optional<CredencialEntity> findByResetToken(String resetToken);

    @Modifying
    @Transactional
    @Query("DELETE FROM CredencialEntity c WHERE c.idCliente = :idCliente")
    void deleteByClienteId(Long idCliente);
}