package org.vitaliistf.twowheels4u.repositories;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.vitaliistf.twowheels4u.models.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("FROM Rental r WHERE (:isActive = false AND r.user.id =:userId "
            + "AND r.actualReturnDate IS NOT NULL) "
            + "OR (:isActive = true AND r.user.id =:userId AND r.actualReturnDate IS NULL)")
    List<Rental> findByIdAndIsActive(Long userId, boolean isActive);

    @Query("FROM Rental r WHERE r.actualReturnDate is NULL AND r.returnDate < :date")
    List<Rental> findOverdueRentals(LocalDateTime date);

}
