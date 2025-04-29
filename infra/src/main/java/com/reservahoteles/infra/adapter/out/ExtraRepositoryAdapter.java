package com.reservahoteles.infra.adapter.out;

import com.reservahoteles.domain.entity.Extra;
import com.reservahoteles.domain.port.out.ExtraRepository;
import com.reservahoteles.infra.entity.ExtraEntity;
import com.reservahoteles.infra.mapper.ExtraMapper;
import com.reservahoteles.infra.repository.SpringExtraRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ExtraRepositoryAdapter implements ExtraRepository {

    private final SpringExtraRepository springExtraRepository;

    public ExtraRepositoryAdapter(SpringExtraRepository springExtraRepository) {
        this.springExtraRepository = springExtraRepository;
    }

    @Override
    public Extra save(Extra extra) {
        ExtraEntity e = ExtraMapper.INSTANCE.toEntity(extra);
        ExtraEntity saved = springExtraRepository.save(e);
        return ExtraMapper.INSTANCE.toDomain(saved);
    }

    @Override
    public Optional<Extra> findById(Long id) {
        return springExtraRepository.findById(id)
                .map(ExtraMapper.INSTANCE::toDomain);
    }

    @Override
    public List<Extra> findAll() {
        return springExtraRepository.findAll().stream()
                .map(ExtraMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Extra> buscarPorNombre(String nombreExtra) {
        return springExtraRepository.findByNombreExtraContainingIgnoreCase(nombreExtra)
                .stream()
                .map(ExtraMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Extra> findByIds(List<Long> ids) {
        return springExtraRepository.findByIdIn(ids)
                .stream()
                .map(ExtraMapper.INSTANCE::toDomain)
                .toList();
    }

    @Override
    public Page<Extra> findAll(Pageable pageable) {
        return springExtraRepository.findAll(pageable)
                .map(ExtraMapper.INSTANCE::toDomain);
    }

    @Override
    public Page<Extra> findByHabitacionId(Long idHabitacion, Pageable pageable) {
        log.debug("Buscando extras para la habitación con ID: {}", idHabitacion);
        try {
            Page<ExtraEntity> extras = springExtraRepository.findByHabitacionId(idHabitacion, pageable);
            log.debug("Encontrados {} extras para la habitación {}", extras.getTotalElements(), idHabitacion);
            return extras.map(ExtraMapper.INSTANCE::toDomain);
        } catch (Exception e) {
            log.error("Error al buscar extras para la habitación {}: {}", idHabitacion, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void eliminar(Long id) {
        springExtraRepository.deleteById(id);
    }
}