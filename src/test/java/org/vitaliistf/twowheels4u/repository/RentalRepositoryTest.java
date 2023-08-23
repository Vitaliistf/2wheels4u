package org.vitaliistf.twowheels4u.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.vitaliistf.twowheels4u.models.Rental;
import org.vitaliistf.twowheels4u.repositories.RentalRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ContainersConfig.class)
class RentalRepositoryTest {
    private static final int EXPECTED_SIZE = 1;
    private static final int NOT_EXPECTED_SIZE = 2;
    private static final Long EXPECTED_ID = 91L;
    private static final Long NOT_EXPECTED_ID = 2L;
    private static final int POSITION = 0;
    private static final boolean ACTIVE = true;
    private static final boolean NOT_ACTIVE = false;
    private static final int YEAR = 2023;
    private static final int MONTH = 1;
    private static final int NOT_EXPECTED_MONTH = 2;
    private static final int DAY_OF_MONTH = 1;
    private static final int RETURN_DAY_OF_MONTH = 2;
    private static final int HOUR = 0;
    private static final int MINUTES = 30;
    private static final int SECONDS = 31;
    private static final int SIZE_OF_OVERDUE_RENTAL = 0;
    private static final int OVERDUE_YEAR = 2022;
    private static final int OVERDUE_TEST_DIGIT = 2;

    @Autowired
    private RentalRepository rentalRepository;

    @BeforeEach
    @Sql("/scripts/clear_tables.sql")
    public void clearTables() {}

    @Test
    @Sql("/scripts/init_rentals.sql")
    void shouldFindByUserIdAndIsActiveTrue() {
        List<Rental> actual = rentalRepository.findByIdAndIsActive(EXPECTED_ID, ACTIVE);
        assertEquals(EXPECTED_SIZE, actual.size());
        assertEquals(EXPECTED_ID, actual.get(POSITION).getUser().getId());
        assertEquals(EXPECTED_ID, actual.get(POSITION).getMotorcycle().getId());
        assertNull(actual.get(POSITION).getActualReturnDate());
        assertEquals(LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, HOUR,
                MINUTES, SECONDS), actual.get(POSITION).getRentalDate());
        assertEquals(LocalDateTime.of(YEAR, MONTH, RETURN_DAY_OF_MONTH, HOUR,
                MINUTES, SECONDS), actual.get(POSITION).getReturnDate());

    }

    @Test
    @Sql("/scripts/init_rentals.sql")
    void shouldFindByUserIdAndIsActiveTrueWrongParameters() {
        List<Rental> actual = rentalRepository.findByIdAndIsActive(EXPECTED_ID, ACTIVE);
        assertNotEquals(NOT_EXPECTED_SIZE, actual.size());
        assertNotEquals(NOT_EXPECTED_ID, actual.get(POSITION).getUser().getId());
        assertNotEquals(NOT_EXPECTED_ID, actual.get(POSITION).getMotorcycle().getId());
        assertNull(actual.get(POSITION).getActualReturnDate());
        assertNotEquals(LocalDateTime.of(YEAR, NOT_EXPECTED_MONTH, DAY_OF_MONTH, HOUR,
                MINUTES, SECONDS), actual.get(POSITION).getRentalDate());
        assertNotEquals(LocalDateTime.of(YEAR, NOT_EXPECTED_MONTH, RETURN_DAY_OF_MONTH, HOUR,
                MINUTES, SECONDS), actual.get(POSITION).getReturnDate());

    }

    @Test
    @Sql("/scripts/init_rentals.sql")
    void shouldFindByUserIdAndIsActiveFalse() {
        List<Rental> actual = rentalRepository.findByIdAndIsActive(EXPECTED_ID, NOT_ACTIVE);
        assertEquals(EXPECTED_SIZE, actual.size());
        assertEquals(EXPECTED_ID, actual.get(POSITION).getUser().getId());
        assertEquals(EXPECTED_ID, actual.get(POSITION).getMotorcycle().getId());
        assertNotNull(actual.get(POSITION).getActualReturnDate());
        assertEquals(LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, HOUR,
                MINUTES, SECONDS), actual.get(POSITION).getRentalDate());
        assertEquals(LocalDateTime.of(YEAR, MONTH, RETURN_DAY_OF_MONTH, HOUR,
                MINUTES, SECONDS), actual.get(POSITION).getReturnDate());

    }

    @Test
    @Sql("/scripts/init_rentals.sql")
    void shouldFindOverdueRental() {
        LocalDateTime now = LocalDateTime.now();
        List<Rental> actual = rentalRepository.findOverdueRentals(now);
        assertEquals(EXPECTED_SIZE, actual.size());
        assertNull(actual.get(POSITION).getActualReturnDate());
    }

    @Test
    @Sql("/scripts/init_rentals.sql")
    void shouldFindOverdueRentalWrongParameters() {
        LocalDateTime now = LocalDateTime.now();
        List<Rental> actual = rentalRepository.findOverdueRentals(now);
        assertNotEquals(NOT_EXPECTED_SIZE, actual.size());
        assertNotNull(actual.get(POSITION).getMotorcycle());
    }

    @Test
    @Sql("/scripts/init_rentals.sql")
    void shouldNotFindOverdueRental() {
        LocalDateTime now = LocalDateTime.of(OVERDUE_YEAR, OVERDUE_TEST_DIGIT, OVERDUE_TEST_DIGIT,
                OVERDUE_TEST_DIGIT, OVERDUE_TEST_DIGIT, OVERDUE_TEST_DIGIT);
        List<Rental> actual = rentalRepository.findOverdueRentals(now);
        assertEquals(SIZE_OF_OVERDUE_RENTAL, actual.size());
    }
}
