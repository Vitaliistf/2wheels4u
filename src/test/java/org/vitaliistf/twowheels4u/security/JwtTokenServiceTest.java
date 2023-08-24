package org.vitaliistf.twowheels4u.security;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.vitaliistf.twowheels4u.model.User.Role;
import org.springframework.security.core.userdetails.User;
import org.vitaliistf.twowheels4u.security.jwt.JwtTokenService;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class JwtTokenServiceTest {
    private static final String EMAIL = "user1@mail.com";
    private static final String PASSWORD = "11111111";
    private static final long VALIDITY_IN_MILLISECONDS = 3600000L;
    private static final String SECRET = "secret";

    @MockBean
    private JwtTokenService jwtTokenService;
    private UserDetailsService userDetailsService;
    private String token;

    @BeforeEach
    void setUp() throws ReflectiveOperationException {
        userDetailsService = Mockito.mock(UserDetailsService.class);

        jwtTokenService = new JwtTokenService(userDetailsService);

        Field secretKey = JwtTokenService.class.getDeclaredField("secretKey");
        secretKey.setAccessible(true);
        secretKey.set(jwtTokenService, SECRET);

        Field validityInMilliseconds = JwtTokenService.class.
                getDeclaredField("validityInMilliSeconds");
        validityInMilliseconds.setAccessible(true);
        validityInMilliseconds.setLong(jwtTokenService, VALIDITY_IN_MILLISECONDS);

        token = jwtTokenService.createToken(EMAIL, Role.CUSTOMER);
    }

    @Test
    void testCreateToken() {
        assertNotNull(token);
        String actual = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        assertEquals(actual, EMAIL);
    }

    @Test
    void testGetUsername() {
        String actual = jwtTokenService.getUsername(token);
        assertEquals(actual, EMAIL);
    }

    @Test
    void testGetAuthentication() {
        UserDetails userDetails = User.withUsername(EMAIL)
                .password(PASSWORD)
                .roles(Role.CUSTOMER.name())
                .build();
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        Authentication actual = jwtTokenService.getAuthentication(token);
        assertNotNull(actual);
        assertTrue(actual.getPrincipal().toString().contains(EMAIL));
    }

    @Test
    void testResolveToken() {
        String bearerToken = "Bearer " + token;
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", bearerToken);
        String actual = jwtTokenService.resolveToken(request);
        assertNotNull(actual);
        assertEquals(actual, token);
    }

    @Test
    void testValidateToken() {
        boolean actual = jwtTokenService.validateToken(token);
        assertTrue(actual);
    }
}