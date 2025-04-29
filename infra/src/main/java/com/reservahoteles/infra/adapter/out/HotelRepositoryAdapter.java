package com.reservahoteles.infra.adapter.out;
import com.reservahoteles.domain.entity.Hotel;
import com.reservahoteles.domain.port.out.HotelRepository;
import com.reservahoteles.infra.entity.HotelEntity;
import com.reservahoteles.infra.mapper.HotelMapper;
import com.reservahoteles.infra.repository.SpringHotelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class HotelRepositoryAdapter implements HotelRepository {

    private final SpringHotelRepository springHotelRepository;

    public HotelRepositoryAdapter(SpringHotelRepository springHotelRepository) {
        this.springHotelRepository = springHotelRepository;
    }

    @Override
    public Hotel save(Hotel hotel) {
        var entity = HotelMapper.INSTANCE.toEntity(hotel);
        var savedEntity = springHotelRepository.save(entity);
        return HotelMapper.INSTANCE.toDomain(savedEntity);
    }

    @Override
    public Optional<Hotel> findById(Long id) {
        return springHotelRepository.findById(id)
                .map(HotelMapper.INSTANCE::toDomain);
    }

    @Override
    public List<Hotel> findAll() {
        return springHotelRepository.findAll().stream()
                .map(HotelMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Hotel> buscarPorCriterios(String pais, String ciudad, Integer numeroEstrellas, String nombre) {
        // Usando el método de consulta personalizada
        return springHotelRepository.buscarPorCriterios(pais, ciudad, numeroEstrellas, nombre)
                .stream()
                .map(HotelMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Hotel> buscarPorCriteriosAvanzados(String pais, String ciudad, Integer numeroEstrellas,
                                                   String nombre, String direccion, String telefono, String email) {
        return springHotelRepository.buscarPorCriteriosAvanzados(pais, ciudad, numeroEstrellas, nombre, direccion, telefono, email)
                .stream()
                .map(HotelMapper.INSTANCE::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Hotel> buscarPorCriteriosAvanzados(String pais,
                                                   String ciudad,
                                                   Integer numeroEstrellas,
                                                   String nombre,
                                                   String direccion,
                                                   String telefono,
                                                   String email,
                                                   Pageable pageable) {
        Page<HotelEntity> page = springHotelRepository.buscarPorCriteriosAvanzados(
                pais, ciudad, numeroEstrellas,
                nombre, direccion, telefono,
                email, pageable
        );
        return page.map(HotelMapper.INSTANCE::toDomain);
    }

    @Override
    public void eliminar(Long id) {
        springHotelRepository.deleteById(id);
    }

    @Override
    public void asignarServicios(Long idHotel, List<Long> idsServicios) {
        // Aquí deberás implementar la lógica para asignar servicios a un hotel,
        // lo cual típicamente implica actualizar una relación muchos a muchos.
        // Dado que este ejemplo es general, se indica un método para asignar.
        // La implementación dependerá de cómo modeles la relación entre Hotel y Servicio en la base de datos.
        throw new UnsupportedOperationException("Asignar servicios aún no implementado.");
    }
}