package org.vitaliistf.twowheels4u.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vitaliistf.twowheels4u.model.Payment;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {
    private Long rentalId;
    private Payment.Type type;
}

