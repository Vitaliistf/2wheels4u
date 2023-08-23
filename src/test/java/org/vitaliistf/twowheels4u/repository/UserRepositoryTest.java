package org.vitaliistf.twowheels4u.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.vitaliistf.twowheels4u.models.User;
import org.vitaliistf.twowheels4u.repositories.UserRepository;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ContainersConfig.class)
class UserRepositoryTest {
    private static final String EMAIL = "user1@mail.com";
    private static final String WRONG_EMAIL = "wrong@mail.com";
    private static final String EXCEPTION_EXPECTED = "NoSuchElementException expected";
    private static final String PASSWORD = "11111111";
    private static final String USER_FIRST_NAME = "firstName1";

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @Sql("/scripts/clear_tables.sql")
    public void clearTables() {}

    @Test
    @Sql("/scripts/init_users.sql")
    void testReturnUserWithEmail() {
        User actual = userRepository.findByEmail(EMAIL).orElseThrow(NoSuchElementException::new);
        assertEquals(EMAIL, actual.getEmail());
        assertEquals(PASSWORD, actual.getPassword());
        assertEquals(USER_FIRST_NAME, actual.getFirstName());
    }

    @Test
    @Sql("/scripts/init_users.sql")
    void testNotFindUserWithWrongEmail() {
        assertThrows(NoSuchElementException.class,
                () -> userRepository.findByEmail(WRONG_EMAIL).orElseThrow(), EXCEPTION_EXPECTED);
    }

    @Test
    @Sql("/scripts/init_users.sql")
    void testNotFoundUserAfterDelete() {
        userRepository.deleteAll();
        assertThrows(NoSuchElementException.class,
                () -> userRepository.findByEmail(EMAIL).orElseThrow(), EXCEPTION_EXPECTED);
    }
}
