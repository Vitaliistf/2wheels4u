package org.vitaliistf.twowheels4u.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vitaliistf.twowheels4u.models.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
}
