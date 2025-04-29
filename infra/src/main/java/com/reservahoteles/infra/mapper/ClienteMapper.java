package com.reservahoteles.infra.mapper;

import com.reservahoteles.domain.entity.Cliente;
import com.reservahoteles.infra.entity.ClienteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClienteMapper {

    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);

    Cliente toDomain(ClienteEntity entity);

    ClienteEntity toEntity(Cliente domain);
}