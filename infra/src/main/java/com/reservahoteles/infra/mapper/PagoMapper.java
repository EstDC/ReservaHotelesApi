package com.reservahoteles.infra.mapper;

import com.reservahoteles.common.enums.EstadoPago;
import com.reservahoteles.domain.entity.Pago;
import com.reservahoteles.infra.entity.PagoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.math.BigDecimal;


@Mapper(componentModel = "spring")
public interface PagoMapper {
    PagoMapper INSTANCE = Mappers.getMapper(PagoMapper.class);

    Pago toDomain(PagoEntity entity);

    PagoEntity toEntity(Pago domain);
}