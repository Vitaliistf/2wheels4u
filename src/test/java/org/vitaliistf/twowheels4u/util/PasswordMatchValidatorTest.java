package org.vitaliistf.twowheels4u.util;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.vitaliistf.twowheels4u.dto.request.UserRegisterRequestDto;
import org.vitaliistf.twowheels4u.util.validation.PasswordMatch;
import org.vitaliistf.twowheels4u.util.validation.PasswordMatchValidator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordMatchValidatorTest {
    private static final String EMAIL = "user1@mail.com";
    private static final String PASSWORD = "123QWEasd";
    private static final String REPEAT_PASSWORD = "123QWEasd";
    private static final String WRONG_PASSWORD = "000000000000";
    private static PasswordMatchValidator passwordValidator;
    private static ConstraintValidatorContext constraintValidatorContext;
    private static PasswordMatch constraintAnnotation;
    private static UserRegisterRequestDto registrationDto;

    @BeforeAll
    static void beforeAll() {
        registrationDto = new UserRegisterRequestDto();
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        passwordValidator = new PasswordMatchValidator();
        constraintAnnotation = Mockito.mock(PasswordMatch.class);
        Mockito.when(constraintAnnotation.password()).thenReturn("password");
        Mockito.when(constraintAnnotation.passwordConfirmation()).thenReturn("repeatPassword");
        passwordValidator.initialize(constraintAnnotation);
    }

    @BeforeEach
    void setUp() {
        registrationDto.setPassword(PASSWORD);
        registrationDto.setEmail(EMAIL);
        registrationDto.setRepeatPassword(REPEAT_PASSWORD);
    }

    @Test
    void testValidationNormalCase() {
        assertTrue(passwordValidator.isValid(registrationDto, constraintValidatorContext));
    }

    @Test
    void testValidationWrongCase() {
        registrationDto.setRepeatPassword(WRONG_PASSWORD);
        assertFalse(passwordValidator.isValid(registrationDto, constraintValidatorContext));
    }
}
