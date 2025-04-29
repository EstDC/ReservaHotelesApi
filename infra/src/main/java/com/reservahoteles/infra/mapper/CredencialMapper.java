package com.reservahoteles.infra.mapper;

import com.reservahoteles.domain.entity.Credencial;
import com.reservahoteles.infra.entity.CredencialEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CredencialMapper {
    CredencialMapper INSTANCE = Mappers.getMapper(CredencialMapper.class);

    @Mapping(target = "activo", source = "activo")
    @Mapping(target = "ultimaActualizacion", source = "ultimaActualizacion")
    @Mapping(target="resetToken",       source="resetToken")
    @Mapping(target="resetTokenExpiry",source="resetTokenExpiry")
    Credencial toDomain(CredencialEntity e);

    @Mapping(target = "activo", source = "activo")
    @Mapping(target = "ultimaActualizacion", source = "ultimaActualizacion")
    @Mapping(target="resetToken",       source="resetToken")
    @Mapping(target="resetTokenExpiry",source="resetTokenExpiry")
    CredencialEntity toEntity(Credencial d);
}