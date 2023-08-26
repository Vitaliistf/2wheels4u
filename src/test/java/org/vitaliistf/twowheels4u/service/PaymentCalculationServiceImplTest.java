package org.vitaliistf.twowheels4u.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vitaliistf.twowheels4u.model.Motorcycle;
import org.vitaliistf.twowheels4u.model.Payment;
import org.vitaliistf.twowheels4u.model.Rental;
import org.vitaliistf.twowheels4u.service.impl.PaymentCalculationServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentCalculationServiceImplTest {
    private static final Long ID = 1L;
    private static final BigDecimal DAILY_FEE = BigDecimal.valueOf(100);
    private static final BigDecimal MONEY_TO_PAY = BigDecimal.valueOf(200);

    private Rental rental;
    private Motorcycle motorcycle;
    private Payment payment;

    @InjectMocks
    private PaymentCalculationServiceImpl paymentCalculationService;

    @Mock
    private RentalService rentalService;

    @Mock
    private MotorcycleService motorcycleService;

    @BeforeEach
    void setUp() {
        motorcycle = new Motorcycle();
        motorcycle.setId(ID);
        motorcycle.setFee(DAILY_FEE);

        rental = new Rental();
        rental.setId(ID);
        rental.setRentalDate(LocalDateTime.now().minusDays(2));
        rental.setReturnDate(LocalDateTime.now());
        rental.setMotorcycle(motorcycle);

        payment = new Payment();
        payment.setRental(rental);

    }

    @Test
    void testCalculatePaymentAmount() {
        when(rentalService.findById(ID)).thenReturn(rental);
        when(motorcycleService.findById(ID)).thenReturn(motorcycle);
        BigDecimal paymentAmount = paymentCalculationService.calculatePaymentAmount(payment);

        assertEquals(MONEY_TO_PAY, paymentAmount);
    }

    @Test
    void testCalculateFineAmount() {
        rental.setReturnDate(LocalDateTime.now().minusDays(1));
        rental.setActualReturnDate(LocalDateTime.now());
        when(rentalService.findById(ID)).thenReturn(rental);

        BigDecimal fineAmount = paymentCalculationService.calculateFineAmount(payment);

        BigDecimal expectedAmount = BigDecimal.valueOf(1.2);
        assertEquals(expectedAmount, fineAmount);
    }
}
