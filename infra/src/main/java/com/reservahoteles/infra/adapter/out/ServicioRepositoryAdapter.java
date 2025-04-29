package com.reservahoteles.infra.adapter.out;

import com.reservahoteles.domain.entity.Servicio;
import com.reservahoteles.domain.port.out.ServicioRepository;
import com.reservahoteles.infra.mapper.ServicioMapper;
import com.reservahoteles.infra.repository.SpringServicioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ServicioRepositoryAdapter implements ServicioRepository {

    private final SpringServicioRepository springRepo;
    private final ServicioMapper mapper;

    public ServicioRepositoryAdapter(SpringServicioRepository springRepo,
                                     ServicioMapper mapper) {
        this.springRepo = springRepo;
        this.mapper     = mapper;
    }

    @Override
    public Servicio save(Servicio servicio) {
        var ent   = mapper.toEntity(servicio);
        var saved = springRepo.save(ent);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Servicio> findById(Long id) {
        return springRepo.findById(id)
                .map(mapper::toDomain);
    }

    // Nuevo: listado paginado
    @Override
    public Page<Servicio> findAll(Pageable pageable) {
        return springRepo.findAll(pageable)
                .map(mapper::toDomain);
    }

    // Nuevo: búsqueda paginada por nombre/descripcion
    @Override
    public Page<Servicio> buscarPorCriterios(String nombre,
                                             String descripcion,
                                             Pageable pageable) {
        return springRepo.buscarPorCriterios(nombre, descripcion, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public List<Servicio> findByIds(List<Long> ids) {
        return springRepo.findAllByIdIn(ids).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        springRepo.deleteById(id);
    }
}