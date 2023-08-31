package org.vitaliistf.twowheels4u.dto.response;

import java.math.BigDecimal;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.vitaliistf.twowheels4u.model.Payment;

@Data
@RequiredArgsConstructor

public class PaymentResponseDto {
    private Long id;
    private Payment.Status status;
    private Payment.Type type;
    private Long rentalId;
    private String sessionUrl;
    private String sessionId;
    private BigDecimal amount;
}
