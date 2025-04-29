package com.reservahoteles.infra.mapper;

import com.reservahoteles.domain.entity.Servicio;
import com.reservahoteles.infra.entity.ServicioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServicioMapper {

    // Mapea entity → domain. No necesitamos ignorar nada aquí,
    // porque la clase Servicio no tiene ninguna lista 'hoteles'.
    Servicio toDomain(ServicioEntity entity);

    // Mapea domain → entity. Ignoramos la propiedad 'hoteles'
    // en la entidad para que no intente poblarla desde el DTO.
    @Mapping(target = "hoteles", ignore = true)
    ServicioEntity toEntity(Servicio domain);
}