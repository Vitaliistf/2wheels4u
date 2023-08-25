package org.vitaliistf.twowheels4u.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.vitaliistf.twowheels4u.model.Motorcycle;
import org.vitaliistf.twowheels4u.model.Rental;
import org.vitaliistf.twowheels4u.repository.RentalRepository;
import org.vitaliistf.twowheels4u.service.MotorcycleService;
import org.vitaliistf.twowheels4u.service.RentalService;

@Service
@AllArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final MotorcycleService motorcycleService;

    @Override
    public Rental save(Rental rental) {
        Motorcycle motorcycleFromDb = motorcycleService.findById(rental.getMotorcycle().getId());
        if (motorcycleFromDb.getInventory() > 0) {
            motorcycleFromDb.setInventory(motorcycleFromDb.getInventory() - 1);
            motorcycleService.update(motorcycleFromDb.getId(), motorcycleFromDb);
            return rentalRepository.save(rental);
        }
        throw new RuntimeException("Cannot save rental, there are no motorcycles in inventory");
    }

    @Override
    public List<Rental> findByIdAndIsActive(Long userId, boolean isActive) {
        return rentalRepository.findByIdAndIsActive(userId, isActive);
    }

    @Override
    public Rental findById(Long id) {
        return rentalRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Cannot find rental by id: " + id));
    }

    @Override
    public Rental returnRental(Long id) {
        Rental rental = findById(id);
        Motorcycle motorcycleToReturn = motorcycleService.findById(rental.getMotorcycle().getId());
        motorcycleToReturn.setInventory(motorcycleToReturn.getInventory() + 1);
        motorcycleService.update(motorcycleToReturn.getId(), motorcycleToReturn);
        rental.setActualReturnDate(LocalDateTime.now());
        return update(id, rental);
    }

    @Override
    public List<Rental> findOverdueRentals(LocalDateTime dateTime) {
        return rentalRepository.findOverdueRentals(dateTime);
    }

    @Override
    public Rental update(Long id, Rental rental) {
        Rental rentalFromDb = rentalRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Cannot find rental by id: " + id));
        rentalFromDb.setMotorcycle(rental.getMotorcycle());
        rentalFromDb.setUser(rental.getUser());
        rentalFromDb.setRentalDate(rental.getRentalDate());
        rentalFromDb.setReturnDate(rental.getReturnDate());
        rentalFromDb.setActualReturnDate(rental.getActualReturnDate());
        return rentalRepository.save(rentalFromDb);
    }

    @Override
    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }
}
