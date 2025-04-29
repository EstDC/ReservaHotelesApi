package com.reservahoteles.infra.adapter.out;

import com.reservahoteles.common.enums.EstadoPago;
import com.reservahoteles.domain.entity.Pago;
import com.reservahoteles.domain.port.out.PagoRepository;
import com.reservahoteles.infra.mapper.PagoMapper;
import com.reservahoteles.infra.repository.SpringPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PagoRepositoryAdapter implements PagoRepository {

    private final SpringPagoRepository springPagoRepository;

    @Override
    public Pago save(Pago pago) {
        var entity = PagoMapper.INSTANCE.toEntity(pago);
        var saved  = springPagoRepository.save(entity);
        return PagoMapper.INSTANCE.toDomain(saved);
    }

    @Override
    public Optional<Pago> findById(Long id) {
        return springPagoRepository.findById(id)
                .map(PagoMapper.INSTANCE::toDomain);
    }

    @Override
    public List<Pago> findAll() {
        return springPagoRepository.findAll().stream()
                .map(PagoMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pago> findAllActivas() {
        return springPagoRepository.findAllByActivoTrue().stream()
                .map(PagoMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Pago> findAllActivas(Pageable pageable) {
        return springPagoRepository.findAllByActivoTrue(pageable)
                .map(PagoMapper.INSTANCE::toDomain);
    }

    @Override
    public Page<Pago> findByActivoTrue(Pageable pageable) {
        return springPagoRepository
                .findAllByActivoTrue(pageable)
                .map(PagoMapper.INSTANCE::toDomain);
    }

    @Override
    public List<Pago> findByIdClienteAndActivoTrue(Long idCliente) {
        return springPagoRepository.findByIdClienteAndActivoTrue(idCliente).stream()
                .map(PagoMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Pago> findByIdClienteAndActivoTrue(Long idCliente, Pageable pageable) {
        return springPagoRepository.findByIdClienteAndActivoTrue(idCliente, pageable)
                .map(PagoMapper.INSTANCE::toDomain);
    }

    @Override
    public List<Pago> buscarPorCriterios(
            Long idReserva,
            Long idCliente,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            EstadoPago estadoPago
    ) {
        return springPagoRepository.buscarPorCriterios(
                        idReserva, idCliente, fechaInicio, fechaFin, estadoPago
                ).stream()
                .map(PagoMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Pago> buscarPorCriterios(
            Long idReserva,
            Long idCliente,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            EstadoPago estadoPago,
            Pageable pageable
    ) {
        return springPagoRepository.buscarPorCriterios(
                idReserva, idCliente, fechaInicio, fechaFin, estadoPago, pageable
        ).map(PagoMapper.INSTANCE::toDomain);
    }

    @Override
    public void eliminar(Long id) {
        springPagoRepository.deleteById(id);
    }
}