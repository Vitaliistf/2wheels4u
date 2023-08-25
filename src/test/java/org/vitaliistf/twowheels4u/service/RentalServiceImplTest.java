package org.vitaliistf.twowheels4u.service;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vitaliistf.twowheels4u.model.Motorcycle;
import org.vitaliistf.twowheels4u.model.Rental;
import org.vitaliistf.twowheels4u.repository.RentalRepository;
import org.vitaliistf.twowheels4u.service.impl.RentalServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalServiceImplTest {
    private static final long EXPECTED_ID = 201L;
    private static final int VALID_AMOUNT_FOR_RENT = 2;
    private static final int AMOUNT_AFTER_CREATING_RENT = 1;
    private static final int NOT_VALID_AMOUNT_FOR_RENT = 0;
    private Rental rental;
    private Motorcycle motorcycle;
    @InjectMocks
    private RentalServiceImpl rentalService;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private MotorcycleService motorcycleService;

    @BeforeEach
    void setUp() {
        motorcycle = new Motorcycle();
        motorcycle.setId(EXPECTED_ID);
        motorcycle.setDeleted(false);
        motorcycle.setType(Motorcycle.Type.STREET);
        motorcycle.setManufacturer("Manufacturer");
        motorcycle.setModel("Model");
        motorcycle.setFee(BigDecimal.TEN);

        rental = new Rental();
        rental.setId(EXPECTED_ID);
        rental.setMotorcycle(motorcycle);
    }

    @Test
    void testValidRentalSaving() {
        motorcycle.setInventory(VALID_AMOUNT_FOR_RENT);
        when(motorcycleService.findById(EXPECTED_ID)).thenReturn(motorcycle);
        when(rentalRepository.save(rental)).thenReturn(rental);
        Rental savedRental = rentalService.save(rental);
        assertNotNull(savedRental);
        assertEquals(rental.getId(), savedRental.getId());
        assertEquals(AMOUNT_AFTER_CREATING_RENT, motorcycle.getInventory());
    }

    @Test
    void testInvalidRentalSaving() {
        motorcycle.setInventory(NOT_VALID_AMOUNT_FOR_RENT);
        when(motorcycleService.findById(EXPECTED_ID)).thenReturn(motorcycle);
        assertThrows(RuntimeException.class, () ->
                rentalService.save(rental), "RuntimeException expected");
    }
}