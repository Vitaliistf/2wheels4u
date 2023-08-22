package org.vitaliistf.twowheels4u.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vitaliistf.twowheels4u.dto.request.MotorcycleRequestDto;
import org.vitaliistf.twowheels4u.dto.response.MotorcycleResponseDto;
import org.vitaliistf.twowheels4u.models.Motorcycle;

@Mapper(config = MapperConfig.class)
public interface MotorcycleMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "model", target = "model")
    @Mapping(source = "manufacturer", target = "manufacturer")
    @Mapping(source = "inventory", target = "inventory")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "fee", target = "fee")
    @Mapping(source = "deleted", target = "deleted")
    MotorcycleResponseDto toDto(Motorcycle motorcycle);

    @Mapping(ignore = true, target = "id")
    @Mapping(source = "model", target = "model")
    @Mapping(source = "manufacturer", target = "manufacturer")
    @Mapping(source = "inventory", target = "inventory")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "fee", target = "fee")
    @Mapping(ignore = true, target = "deleted")
    Motorcycle toModel(MotorcycleRequestDto dto);
}
