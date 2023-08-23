package org.vitaliistf.twowheels4u.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.vitaliistf.twowheels4u.models.Motorcycle;
import org.vitaliistf.twowheels4u.repositories.MotorcycleRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ContainersConfig.class)
class MotorcycleRepositoryTest {
    private static final int SIZE = 63;
    private static final int WRONG_SIZE = 65;
    private static final Long ID_FOR_DELETION = 91L;
    private static final Long ID_OF_ALREADY_DELETED_MOTORCYCLE = 92L;

    @Autowired
    private MotorcycleRepository motorcycleRepository;

    @BeforeEach
    @Sql("/scripts/clear_tables.sql")
    public void clearTables() {}

    @Test
    @Sql("/scripts/init_motorcycles.sql")
    public void shouldDelete() {
        motorcycleRepository.safeDelete(ID_FOR_DELETION);
        Optional<Motorcycle> actual = motorcycleRepository.findByIdAndDeletedFalse(ID_FOR_DELETION);
        assertFalse(actual.isPresent());
    }

    @Test
    @Sql("/scripts/init_motorcycles.sql")
    public void shouldFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Motorcycle> actual = motorcycleRepository.findAllByDeletedFalse(pageable);
        Assertions.assertEquals(SIZE, actual.getTotalElements());
    }

    @Test
    @Sql("/scripts/init_motorcycles.sql")
    public void shouldNotIncludeDeleted() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Motorcycle> actual = motorcycleRepository.findAllByDeletedFalse(pageable);
        assertNotEquals(WRONG_SIZE, actual.getTotalElements());
    }

    @Test
    @Sql("/scripts/init_motorcycles.sql")
    public void shouldNotFindById() {
        Optional<Motorcycle> actual =
                motorcycleRepository.findByIdAndDeletedFalse(ID_OF_ALREADY_DELETED_MOTORCYCLE);
        assertFalse(actual.isPresent());
    }
}
