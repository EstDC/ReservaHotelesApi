package com.reservahoteles.infra.mapper;

import com.reservahoteles.domain.entity.Habitacion;
import com.reservahoteles.infra.entity.HabitacionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface HabitacionMapper {
    HabitacionMapper INSTANCE = Mappers.getMapper(HabitacionMapper.class);

    @Mapping(target = "precioPorNoche", source = "precioPorNoche")
    @Mapping(target = "capacidad", source = "capacidad")
    Habitacion toDomain(HabitacionEntity entity);

    @Mapping(target = "precioPorNoche", source = "precioPorNoche")
    @Mapping(target = "capacidad", source = "capacidad")
    HabitacionEntity toEntity(Habitacion domain);

    @Named("bigDecimalToDouble")
    default Double bigDecimalToDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : null;
    }

    @Named("doubleToBigDecimal")
    default BigDecimal doubleToBigDecimal(Double value) {
        return value != null ? BigDecimal.valueOf(value) : null;
    }

    @Named("longToInteger")
    default Integer longToInteger(Long value) {
        return value != null ? value.intValue() : null;
    }

    @Named("integerToLong")
    default Long integerToLong(Integer value) {
        return value != null ? value.longValue() : null;
    }
}