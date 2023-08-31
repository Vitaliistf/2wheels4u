package org.vitaliistf.twowheels4u.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.vitaliistf.twowheels4u.model.Payment;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    private Long rentalId;
    private Payment.Type type;
}

