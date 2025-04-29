package com.reservahoteles.infra.mapper;

import com.reservahoteles.domain.entity.Reserva;
import com.reservahoteles.infra.entity.ReservaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReservaMapper {
    ReservaMapper INSTANCE = Mappers.getMapper(ReservaMapper.class);

    @Mapping(target = "total", expression = "java(entity.getTotal().doubleValue())")
    Reserva toDomain(ReservaEntity entity);

    @Mapping(target = "total", expression = "java(java.math.BigDecimal.valueOf(domain.getTotal()))")
    ReservaEntity toEntity(Reserva domain);
}