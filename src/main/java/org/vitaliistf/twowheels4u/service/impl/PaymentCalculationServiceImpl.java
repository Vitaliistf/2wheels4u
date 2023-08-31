package org.vitaliistf.twowheels4u.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vitaliistf.twowheels4u.model.Motorcycle;
import org.vitaliistf.twowheels4u.model.Payment;
import org.vitaliistf.twowheels4u.model.Rental;
import org.vitaliistf.twowheels4u.service.MotorcycleService;
import org.vitaliistf.twowheels4u.service.PaymentCalculationService;
import org.vitaliistf.twowheels4u.service.RentalService;

@Service
@RequiredArgsConstructor
public class PaymentCalculationServiceImpl implements PaymentCalculationService {
    private static final double FINE_MULTIPLIER = 1.2;

    private final RentalService rentalService;
    private final MotorcycleService motorcycleService;

    @Override
    public BigDecimal calculatePaymentAmount(Payment payment) {
        Rental rental = rentalService.findById(payment.getRental().getId());

        Motorcycle motorcycle = motorcycleService.findById(rental.getMotorcycle().getId());

        long daysRental = ChronoUnit.DAYS.between(rental.getRentalDate(), rental.getReturnDate());
        return motorcycle.getFee().multiply(BigDecimal.valueOf(daysRental));
    }

    @Override
    public BigDecimal calculateFineAmount(Payment payment) {
        Rental rental = rentalService.findById(payment.getRental().getId());

        long daysActual;
        if (rental.getActualReturnDate() != null) {
            daysActual = ChronoUnit.DAYS.between(
                    rental.getReturnDate(), rental.getActualReturnDate()
            );
        } else {
            daysActual = 0;
        }

        BigDecimal dailyFee = rental.getMotorcycle().getFee();

        return dailyFee
                .multiply(BigDecimal.valueOf((daysActual) * FINE_MULTIPLIER))
                .divide(BigDecimal.valueOf(100), RoundingMode.UP);
    }
}
