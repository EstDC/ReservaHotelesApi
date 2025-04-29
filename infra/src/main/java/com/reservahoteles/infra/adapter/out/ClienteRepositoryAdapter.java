package com.reservahoteles.infra.adapter.out;

import com.reservahoteles.domain.entity.Cliente;
import com.reservahoteles.infra.mapper.ClienteMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import com.reservahoteles.domain.port.out.ClienteRepository;
import com.reservahoteles.infra.repository.SpringClienteRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ClienteRepositoryAdapter implements ClienteRepository {

    private final SpringClienteRepository springClienteRepository;

    public ClienteRepositoryAdapter(SpringClienteRepository springClienteRepository) {
        this.springClienteRepository = springClienteRepository;
    }

    @Override
    public Cliente save(Cliente cliente) {
        var entity = ClienteMapper.INSTANCE.toEntity(cliente);
        var savedEntity = springClienteRepository.save(entity);
        return ClienteMapper.INSTANCE.toDomain(savedEntity);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return springClienteRepository.findById(id)
                .map(ClienteMapper.INSTANCE::toDomain);
    }

    @Override
    public List<Cliente> findAll() {
        return springClienteRepository.findAll().stream()
                .map(ClienteMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Cliente> findAll(Pageable pageable) {
        return springClienteRepository.findAll(pageable)
                .map(ClienteMapper.INSTANCE::toDomain);
    }

    @Override
    public List<Cliente> buscarPorCriterios(String email, String identificacion, String telefono, String nombre) {
        // Puedes elegir usar el método derivado o el método con @Query.
        // Usando el método customizado:
        return springClienteRepository.buscarPorCriterios(email, identificacion, telefono, nombre)
                .stream()
                .map(ClienteMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Cliente> buscarPorCriterios(
            String email,
            String identificacion,
            String telefono,
            String nombre,
            Pageable pageable
    ) {
        return springClienteRepository.buscarPorCriterios(
                email, identificacion, telefono, nombre, pageable
        ).map(ClienteMapper.INSTANCE::toDomain);
    }

    @Override
    public void eliminar(Long id) {
        springClienteRepository.deleteById(id);
    }

    @Override
    public void eliminarPorNombre(String nombre) {
        springClienteRepository.deleteByNombre(nombre);
    }
}