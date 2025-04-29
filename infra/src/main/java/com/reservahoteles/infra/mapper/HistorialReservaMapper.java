package com.reservahoteles.infra.mapper;

import com.reservahoteles.domain.entity.HistorialReserva;
import com.reservahoteles.infra.entity.HistorialReservaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HistorialReservaMapper {
    HistorialReservaMapper INSTANCE = Mappers.getMapper(HistorialReservaMapper.class);

    // Conversión de entidad de persistencia a dominio
    @Mapping(target = "total", expression = "java(entity.getTotal().doubleValue())")
    HistorialReserva toDomain(HistorialReservaEntity entity);

    // Conversión de dominio a entidad de persistencia
    @Mapping(target = "total", expression = "java(java.math.BigDecimal.valueOf(domain.getTotal()))")
    HistorialReservaEntity toEntity(HistorialReserva domain);
}