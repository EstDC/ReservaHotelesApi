package com.reservahoteles.infra.mapper;

import com.reservahoteles.domain.entity.Hotel;
import com.reservahoteles.infra.entity.HotelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

// Indica a MapStruct que use también estos mappers para las colecciones
@Mapper(
        componentModel = "spring",
        uses = { HabitacionMapper.class, ServicioMapper.class }
)
public interface HotelMapper {

    HotelMapper INSTANCE = Mappers.getMapper(HotelMapper.class);

    // Mapear de persistencia -> dominio (incluye colecciones y fechaRegistro)
    Hotel toDomain(HotelEntity entity);

    // Mapear de dominio -> persistencia (incluye colecciones y fechaRegistro)
    HotelEntity toEntity(Hotel domain);
}