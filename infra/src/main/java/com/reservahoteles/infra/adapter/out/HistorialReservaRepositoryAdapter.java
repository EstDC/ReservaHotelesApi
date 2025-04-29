package com.reservahoteles.infra.adapter.out;

import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.domain.entity.HistorialReserva;
import com.reservahoteles.domain.port.out.HistorialReservaRepository;
import com.reservahoteles.infra.mapper.HistorialReservaMapper;
import com.reservahoteles.infra.repository.SpringHistorialReservaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class HistorialReservaRepositoryAdapter implements HistorialReservaRepository {

    private final SpringHistorialReservaRepository springHistorialReservaRepository;

    public HistorialReservaRepositoryAdapter(SpringHistorialReservaRepository springHistorialReservaRepository) {
        this.springHistorialReservaRepository = springHistorialReservaRepository;
    }

    @Override
    public HistorialReserva save(HistorialReserva historialReserva) {
        var entity       = HistorialReservaMapper.INSTANCE.toEntity(historialReserva);
        var savedEntity  = springHistorialReservaRepository.save(entity);
        return HistorialReservaMapper.INSTANCE.toDomain(savedEntity);
    }

    @Override
    public Optional<HistorialReserva> findById(Long id) {
        return springHistorialReservaRepository.findById(id)
                .map(HistorialReservaMapper.INSTANCE::toDomain);
    }

    @Override
    public List<HistorialReserva> findAll() {
        return springHistorialReservaRepository.findAll().stream()
                .map(HistorialReservaMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistorialReserva> findByClienteId(Long idCliente) {
        return springHistorialReservaRepository.findByIdCliente(idCliente).stream()
                .map(HistorialReservaMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<HistorialReserva> findByClienteId(Long idCliente, Pageable pageable) {
        return springHistorialReservaRepository.findByIdCliente(idCliente, pageable)
                .map(HistorialReservaMapper.INSTANCE::toDomain);
    }

    @Override
    public List<HistorialReserva> buscarPorCriteriosAvanzados(
            Long idReserva,
            Long idCliente,
            Long idHabitacion,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            EstadoReserva estadoReserva
    ) {
        return springHistorialReservaRepository.buscarPorCriteriosAvanzados(
                        idReserva, idCliente, idHabitacion, fechaInicio, fechaFin, estadoReserva
                ).stream()
                .map(HistorialReservaMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<HistorialReserva> buscarPorCriteriosAvanzados(
            Long idReserva,
            Long idCliente,
            Long idHabitacion,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            EstadoReserva estadoReserva,
            Pageable pageable
    ) {
        return springHistorialReservaRepository.buscarPorCriteriosAvanzados(
                idReserva, idCliente, idHabitacion, fechaInicio, fechaFin, estadoReserva, pageable
        ).map(HistorialReservaMapper.INSTANCE::toDomain);
    }

    @Override
    public void eliminar(Long id) {
        springHistorialReservaRepository.deleteById(id);
    }
}