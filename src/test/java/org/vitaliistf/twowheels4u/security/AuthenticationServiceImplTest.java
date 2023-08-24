package org.vitaliistf.twowheels4u.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vitaliistf.twowheels4u.exception.AuthenticationException;
import org.vitaliistf.twowheels4u.model.User;
import org.vitaliistf.twowheels4u.security.impl.AuthenticationServiceImpl;
import org.vitaliistf.twowheels4u.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthenticationServiceImplTest {
    private static final Long ID = 1L;
    private static final String EMAIL = "user1@mail.com";
    private static final String PASSWORD = "11111111";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EXCEPTION_EXPECTED = "AuthenticationException expected";
    private static AuthenticationService authenticationService;
    private static UserService userService;
    private static PasswordEncoder passwordEncoder;
    private User user;

    @BeforeAll
    public static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authenticationService = new AuthenticationServiceImpl(userService, passwordEncoder);
    }

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(ID);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRole(User.Role.CUSTOMER);
    }

    @Test
    public void testRegister() {
        when(userService.save(any())).thenReturn(user);

        User actual = authenticationService.register(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME);
        assertNotNull(actual);
        assertEquals(ID, actual.getId());
        assertEquals(EMAIL, actual.getEmail());
        assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    public void testLogin() {
        user.setPassword(passwordEncoder.encode(PASSWORD));
        when(userService.findByEmail(EMAIL)).thenReturn(user);
        User actual = null;
        try {
            actual = authenticationService.login(EMAIL, PASSWORD);
        } catch (AuthenticationException e) {
            Assertions.fail();
        }
        assertEquals(EMAIL, actual.getEmail());
    }

    @Test
    public void testLoginWrongInput() {
        user.setPassword(passwordEncoder.encode(PASSWORD));
        when(userService.findByEmail(EMAIL)).thenReturn(user);
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(EMAIL, "ERROR"), EXCEPTION_EXPECTED);
    }
}
