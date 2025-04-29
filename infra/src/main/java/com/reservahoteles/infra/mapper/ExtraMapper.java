package com.reservahoteles.infra.mapper;

import com.reservahoteles.domain.entity.Extra;
import com.reservahoteles.infra.entity.ExtraEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import java.math.BigDecimal;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = { BigDecimal.class }
)
public interface ExtraMapper {

    ExtraMapper INSTANCE = Mappers.getMapper(ExtraMapper.class);

    @Mapping(target = "costoAdicional", 
            expression = "java(entity.getCostoAdicional() != null ? entity.getCostoAdicional().doubleValue() : 0.0)")
    Extra toDomain(ExtraEntity entity);

    @Mapping(target = "costoAdicional", 
            expression = "java(domain.getCostoAdicional() != null ? BigDecimal.valueOf(domain.getCostoAdicional()) : BigDecimal.ZERO)")
    ExtraEntity toEntity(Extra domain);
}