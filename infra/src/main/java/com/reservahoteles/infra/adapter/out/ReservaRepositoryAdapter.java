package com.reservahoteles.infra.adapter.out;

import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.domain.entity.Reserva;
import com.reservahoteles.domain.port.out.ReservaRepository;
import com.reservahoteles.infra.mapper.ReservaMapper;
import com.reservahoteles.infra.repository.SpringReservaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ReservaRepositoryAdapter implements ReservaRepository {

    private final SpringReservaRepository springReservaRepository;

    public ReservaRepositoryAdapter(SpringReservaRepository springReservaRepository) {
        this.springReservaRepository = springReservaRepository;
    }

    @Override
    public Reserva save(Reserva reserva) {
        var entity = ReservaMapper.INSTANCE.toEntity(reserva);
        var savedEntity = springReservaRepository.save(entity);
        return ReservaMapper.INSTANCE.toDomain(savedEntity);
    }

    @Override
    public Optional<Reserva> findById(Long id) {
        return springReservaRepository.findById(id)
                .map(ReservaMapper.INSTANCE::toDomain);
    }

    @Override
    public List<Reserva> findAll() {
        return springReservaRepository.findAll()
                .stream()
                .map(ReservaMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reserva> buscarPorCriterios(Long idCliente,
                                            LocalDate fechaInicio,
                                            LocalDate fechaFin,
                                            EstadoReserva estadoReserva) {
        return springReservaRepository
                .buscarPorCriteriosAvanzados(
                        idCliente,
                        null,          // idHabitacion
                        fechaInicio,
                        fechaFin,
                        estadoReserva
                ).stream()
                .map(ReservaMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reserva> buscarPorCriteriosAvanzados(Long idCliente, Long idHabitacion, LocalDate fechaInicio, LocalDate fechaFin, EstadoReserva estadoReserva) {
        return springReservaRepository.buscarPorCriteriosAvanzados(idCliente, idHabitacion, fechaInicio, fechaFin, estadoReserva)
                .stream()
                .map(ReservaMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Reserva> buscarPorCriteriosAvanzados(
            Long idCliente,
            Long idHabitacion,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            EstadoReserva estadoReserva,
            Pageable pageable
    ) {
        return springReservaRepository.buscarPorCriteriosAvanzados(
                        idCliente, idHabitacion, fechaInicio, fechaFin, estadoReserva, pageable
                )
                .map(ReservaMapper.INSTANCE::toDomain);
    }

    @Override
    public void eliminar(Long id) {
        springReservaRepository.deleteById(id);
    }
}