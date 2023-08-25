package org.vitaliistf.twowheels4u.service;

import java.time.LocalDateTime;
import java.util.List;
import org.vitaliistf.twowheels4u.model.Rental;

public interface RentalService {
    Rental save(Rental rental);

    List<Rental> findByIdAndIsActive(Long userId, boolean isActive);

    Rental findById(Long id);

    Rental returnRental(Long id);

    List<Rental> findOverdueRentals(LocalDateTime dateTime);

    Rental update(Long id, Rental rental);

    List<Rental> findAll();
}
