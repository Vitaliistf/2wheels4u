package org.vitaliistf.twowheels4u.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.vitaliistf.twowheels4u.model.Payment;
import org.vitaliistf.twowheels4u.repository.PaymentRepository;
import org.vitaliistf.twowheels4u.service.PaymentService;

@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public void delete(Payment payment) {
        paymentRepository.delete(payment);
    }

    @Override
    public Payment getById(Long id) {
        return paymentRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Cannot find payment by id: " + id)
        );
    }

    @Override
    public List<Payment> getByUserId(Long userId) {
        return paymentRepository.findPaymentByRentalUserId(userId);
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }
}
