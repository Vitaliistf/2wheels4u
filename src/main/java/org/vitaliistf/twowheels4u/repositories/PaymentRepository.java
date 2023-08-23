package org.vitaliistf.twowheels4u.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vitaliistf.twowheels4u.models.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findPaymentByRentalUserId(Long userId);

}
