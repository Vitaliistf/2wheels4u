package org.vitaliistf.twowheels4u.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vitaliistf.twowheels4u.dto.request.RentalRequestDto;
import org.vitaliistf.twowheels4u.dto.response.RentalResponseDto;
import org.vitaliistf.twowheels4u.model.Rental;

@Mapper(config = MapperConfig.class)
public interface RentalMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "returnDate", target = "returnDate")
    @Mapping(source = "rentalDate", target = "rentalDate")
    @Mapping(source = "actualReturnDate", target = "actualReturnDate")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "motorcycle.id", target = "motorcycleId")
    RentalResponseDto toDto(Rental rental);

    @Mapping(ignore = true, target = "id")
    @Mapping(source = "rentalDate", target = "rentalDate")
    @Mapping(source = "returnDate", target = "returnDate")
    @Mapping(source = "motorcycleId", target = "motorcycle.id")
    @Mapping(ignore = true, target = "actualReturnDate")
    @Mapping(ignore = true, target = "user")
    Rental toModel(RentalRequestDto dto);
}
