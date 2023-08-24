package org.vitaliistf.twowheels4u.service;

import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vitaliistf.twowheels4u.model.Motorcycle;
import org.vitaliistf.twowheels4u.repository.MotorcycleRepository;
import org.vitaliistf.twowheels4u.service.impl.MotorcycleServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MotorcycleServiceImplTest {
    private static final Long ID = 1L;
    private static final String MANUFACTURER = "Yamaha";
    private static final String MODEL = "R1";
    private static final int INVENTORY = 5;
    private static final BigDecimal FEE = BigDecimal.valueOf(100);

    private Motorcycle expectedMotorcycle;
    @InjectMocks
    private MotorcycleServiceImpl motorcycleService;

    @Mock
    private MotorcycleRepository motorcycleRepository;


    @BeforeEach
    void setUp() {
        expectedMotorcycle = new Motorcycle();
        expectedMotorcycle.setId(ID);
        expectedMotorcycle.setManufacturer(MANUFACTURER);
        expectedMotorcycle.setFee(FEE);
        expectedMotorcycle.setType(Motorcycle.Type.CRUISER);
        expectedMotorcycle.setModel(MODEL);
        expectedMotorcycle.setInventory(INVENTORY);
        expectedMotorcycle.setDeleted(false);
    }

    @Test
    public void testRemoveMotorcycleFromInventory() {
        when(motorcycleRepository.save(expectedMotorcycle))
                .thenReturn(expectedMotorcycle);
        when(motorcycleRepository.findByIdAndDeletedFalse(ID))
                .thenReturn(Optional.of(expectedMotorcycle));
        Motorcycle motorcycle = motorcycleService.removeMotorcycleFromInventory(ID);
        assertEquals(INVENTORY - 1, motorcycle.getInventory());
    }

    @Test
    public void testRemoveMotorcycleFromInventoryInvalidInventoryCase() {
        expectedMotorcycle.setInventory(0);
        when(motorcycleRepository.findByIdAndDeletedFalse(ID))
                .thenReturn(Optional.of(expectedMotorcycle));
        assertThrows(RuntimeException.class,
                () -> motorcycleService.removeMotorcycleFromInventory(ID));
    }
}
