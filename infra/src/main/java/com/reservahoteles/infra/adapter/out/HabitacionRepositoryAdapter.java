package com.reservahoteles.infra.adapter.out;

import com.reservahoteles.common.enums.EstadoHabitacion;
import com.reservahoteles.common.enums.TipoHabitacion;
import com.reservahoteles.domain.entity.Habitacion;
import com.reservahoteles.domain.port.out.HabitacionRepository;
import com.reservahoteles.infra.entity.HabitacionEntity;
import com.reservahoteles.infra.mapper.HabitacionMapper;
import com.reservahoteles.infra.repository.SpringHabitacionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class HabitacionRepositoryAdapter implements HabitacionRepository {

    private final SpringHabitacionRepository repo;

    public HabitacionRepositoryAdapter(SpringHabitacionRepository repo) {
        this.repo = repo;
    }

    /* ─────────────── CRUD básico ─────────────── */

    @Override
    public Habitacion save(Habitacion habitacion) {
        var entidad  = HabitacionMapper.INSTANCE.toEntity(habitacion);
        var guardada = repo.save(entidad);
        return HabitacionMapper.INSTANCE.toDomain(guardada);
    }

    @Override
    public Optional<Habitacion> findById(Long id) {
        return repo.findById(id)
                .map(HabitacionMapper.INSTANCE::toDomain);
    }

    @Override
    public List<Habitacion> findAll() {
        return repo.findAll().stream()
                .map(HabitacionMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    /* ───────────── Métodos paginados ───────────── */

    @Override
    public Page<Habitacion> buscarPorHotel(
            Long idHotel,
            EstadoHabitacion estado,
            TipoHabitacion tipo,
            Pageable pageable
    ) {
        // Llamamos siempre al método avanzado paginado,
        // dejando nulos los filtros que no aplican.
        Page<HabitacionEntity> page = repo.buscarPorCriteriosAvanzados(
                idHotel,
                estado,                // puede ser null
                tipo,                  // puede ser null
                null,                  // capacidadMin
                null,                  // capacidadMax
                null,                  // precioMin
                null,                  // precioMax
                pageable
        );

        return page.map(HabitacionMapper.INSTANCE::toDomain);
    }

    @Override
    public Page<Habitacion> findByIdHotel(Long idHotel, Pageable pageable) {
        return repo.findByIdHotel(idHotel, pageable)
                .map(HabitacionMapper.INSTANCE::toDomain);
    }

    @Override
    public Page<Habitacion> findByIdHotelAndEstado(
            Long idHotel,
            EstadoHabitacion estado,
            Pageable pageable
    ) {
        return repo.findByIdHotelAndEstado(idHotel, estado, pageable)
                .map(HabitacionMapper.INSTANCE::toDomain);
    }

    @Override
    public Page<Habitacion> findByIdHotelAndTipo(
            Long idHotel,
            TipoHabitacion tipo,
            Pageable pageable
    ) {
        return repo.findByIdHotelAndTipo(idHotel, tipo, pageable)
                .map(HabitacionMapper.INSTANCE::toDomain);
    }

    @Override
    public Page<Habitacion> buscarPorCriterios(
            Long idHotel,
            String numeroHabitacion,
            Long capacidad,
            BigDecimal precioMin,
            EstadoHabitacion estado,
            Pageable pageable
    ) {
        return repo.buscarPorCriterios(idHotel, numeroHabitacion, capacidad, precioMin, estado, pageable)
                .map(HabitacionMapper.INSTANCE::toDomain);
    }

    @Override
    public Page<Habitacion> buscarPorCriteriosAvanzados(
            Long idHotel,
            EstadoHabitacion estado,
            TipoHabitacion tipo,
            Long capacidadMin,
            Long capacidadMax,
            BigDecimal precioMin,
            BigDecimal precioMax,
            Pageable pageable
    ) {
        return repo.buscarPorCriteriosAvanzados(
                        idHotel,
                        estado,
                        tipo,
                        capacidadMin,
                        capacidadMax,
                        precioMin,
                        precioMax,
                        pageable
                )
                .map(HabitacionMapper.INSTANCE::toDomain);
    }

    /* ─────────── Métodos no paginados ─────────── */

    @Override
    public List<Habitacion> findByIdHotel(Long idHotel) {
        return repo.findByIdHotel(idHotel).stream()
                .map(HabitacionMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Habitacion> findByIdHotelAndEstado(
            Long idHotel,
            EstadoHabitacion estado
    ) {
        return repo.findByIdHotelAndEstado(idHotel, estado).stream()
                .map(HabitacionMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Habitacion> findByIdHotelAndTipo(
            Long idHotel,
            TipoHabitacion tipo
    ) {
        return repo.findByIdHotelAndTipo(idHotel, tipo).stream()
                .map(HabitacionMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Habitacion> buscarPorCriterios(
            Long idHotel,
            String numeroHabitacion,
            Long capacidad,
            BigDecimal precioMin,
            EstadoHabitacion estado
    ) {
        return repo.buscarPorCriterios(
                        idHotel,
                        numeroHabitacion,
                        capacidad,
                        precioMin,
                        estado
                ).stream()
                .map(HabitacionMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Habitacion> buscarPorCriteriosAvanzados(
            Long idHotel,
            EstadoHabitacion estado,
            TipoHabitacion tipo,
            Long capacidadMin,
            Long capacidadMax,
            BigDecimal precioMin,
            BigDecimal precioMax
    ) {
        return repo.buscarPorCriteriosAvanzados(
                        idHotel,
                        estado,
                        tipo,
                        capacidadMin,
                        capacidadMax,
                        precioMin,
                        precioMax
                ).stream()
                .map(HabitacionMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }
}