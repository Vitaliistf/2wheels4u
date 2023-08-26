package org.vitaliistf.twowheels4u.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vitaliistf.twowheels4u.dto.request.PaymentRequestDto;
import org.vitaliistf.twowheels4u.dto.response.PaymentResponseDto;
import org.vitaliistf.twowheels4u.model.Payment;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "rental.id", target = "rentalId")
    @Mapping(source = "url", target = "sessionUrl")
    @Mapping(source = "sessionId", target = "sessionId")
    @Mapping(source = "paymentAmount", target = "amount")
    PaymentResponseDto toDto(Payment payment);

    @Mapping(ignore = true, target = "id")
    @Mapping(source = "rentalId", target = "rental.id")
    @Mapping(ignore = true, target = "sessionId")
    @Mapping(ignore = true, target = "url")
    @Mapping(ignore = true, target = "paymentAmount")
    @Mapping(source = "type", target = "type")
    @Mapping(ignore = true, target = "status")
    Payment toModel(PaymentRequestDto dto);
}
