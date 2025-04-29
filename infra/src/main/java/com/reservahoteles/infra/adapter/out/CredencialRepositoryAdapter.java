package com.reservahoteles.infra.adapter.out;

import com.reservahoteles.domain.entity.Credencial;
import com.reservahoteles.domain.port.out.CredencialRepository;
import com.reservahoteles.infra.mapper.CredencialMapper;
import com.reservahoteles.infra.repository.SpringCredencialRepository;
import jakarta.persistence.Column;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class CredencialRepositoryAdapter implements CredencialRepository {

    private final SpringCredencialRepository springCredencialRepository;

    public CredencialRepositoryAdapter(SpringCredencialRepository springCredencialRepository) {
        this.springCredencialRepository = springCredencialRepository;
    }

    @Override
    public Credencial save(Credencial credencial) {
        var entity = CredencialMapper.INSTANCE.toEntity(credencial);
        var saved = springCredencialRepository.save(entity);
        return CredencialMapper.INSTANCE.toDomain(saved);
    }

    @Override
    public Optional<Credencial> findByUsername(String username) {
        return springCredencialRepository.findByUsername(username)
                .map(CredencialMapper.INSTANCE::toDomain);
    }

    @Override
    public Optional<Credencial> findById(Long id) {
        return springCredencialRepository.findById(id)
                .map(CredencialMapper.INSTANCE::toDomain);
    }

    @Override
    public Optional<Credencial> findByClienteId(Long clienteId) {
        return springCredencialRepository.findByIdCliente(clienteId)
                .map(CredencialMapper.INSTANCE::toDomain);
    }

    @Override
    public Optional<Credencial> findByResetToken(String token) {
        return springCredencialRepository
                .findByResetToken(token)
                .map(CredencialMapper.INSTANCE::toDomain);
    }

    @Override
    public void eliminar(Long id) {
        springCredencialRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarPorClienteId(Long clienteId) {
        springCredencialRepository.deleteByClienteId(clienteId);
    }

}