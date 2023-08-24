package org.vitaliistf.twowheels4u.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.vitaliistf.twowheels4u.model.Payment;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ContainersConfig.class)
class PaymentRepositoryTest {
    private static final int EXPECTED_SIZE = 1;
    private static final int NOT_EXPECTED_SIZE = 2;
    private static final Long EXPECTED_ID = 91L;
    private static final Long NOT_EXPECTED_ID = 2L;
    private static final int POSITION = 0;
    private static final String EXPECTED_EXCEPTION = "IndexOutOfBoundsException expected";

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    @Sql("/scripts/clear_tables.sql")
    public void clearTables() {}

    @Test
    @Sql("/scripts/init_payments.sql")
    void testFindPaymentByRentalUserId() {
        List<Payment> actual = paymentRepository.findPaymentByRentalUserId(EXPECTED_ID);
        assertEquals(EXPECTED_SIZE, actual.size());
        assertEquals(EXPECTED_ID, actual.get(POSITION).getRental().getUser().getId());
    }

    @Test
    @Sql("/scripts/init_payments.sql")
    void testFindPaymentByRentalUserIdWrongParameters() {
        List<Payment> actual = paymentRepository.findPaymentByRentalUserId(EXPECTED_ID);
        assertNotEquals(NOT_EXPECTED_SIZE, actual.size());
        assertNotEquals(NOT_EXPECTED_ID, actual.get(POSITION).getRental().getUser().getId());
    }

    @Test
    @Sql("/scripts/init_payments.sql")
    void testNotFindPaymentByWrongRentalUserId() {
        List<Payment> actual = paymentRepository.findPaymentByRentalUserId(NOT_EXPECTED_ID);
        assertEquals(0, actual.size());
        assertThrows(IndexOutOfBoundsException.class,
                () -> actual.get(POSITION).getRental().getUser().getId(), EXPECTED_EXCEPTION);
    }
}
