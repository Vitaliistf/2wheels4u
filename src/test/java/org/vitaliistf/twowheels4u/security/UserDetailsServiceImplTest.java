package org.vitaliistf.twowheels4u.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.vitaliistf.twowheels4u.model.User;
import org.vitaliistf.twowheels4u.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    private static final String EMAIL = "user1@mail.com";
    private static final String PASSWORD = "11111111";
    private static final String EXCEPTION_EXPECTED = "UsernameNotFoundException expected.";

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRole(User.Role.CUSTOMER);
    }

    @Test
    void testLoadUserByUsername() {
        when(userService.findByEmail(EMAIL)).thenReturn(user);

        UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);
        assertNotNull(actual);
        assertEquals(EMAIL, actual.getUsername());
        assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void testNotLoadUserByUsername() {
        when(userService.findByEmail(EMAIL)).thenThrow(UsernameNotFoundException.class);

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(EMAIL), EXCEPTION_EXPECTED);
    }
}
