package org.vitaliistf.twowheels4u.util;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vitaliistf.twowheels4u.util.validation.EmailValidator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class EmailValidatorTest {
    private static final String EMAIL = "user1@mail.com";
    @InjectMocks
    private EmailValidator emailValidator;
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;


    @Test
    void testValidationNormalCase() {
        assertTrue(emailValidator.isValid(EMAIL, constraintValidatorContext));
    }

    @Test
    void testValidationWithoutSnailCase() {
        assertFalse(emailValidator.isValid("user1mail.com", constraintValidatorContext));
    }

    @Test
    void testValidationWithoutDotCase() {
        assertFalse(emailValidator.isValid("user1@mailcom", constraintValidatorContext));
    }

    @Test
    void testValidationEmptyFirstPartCase() {
        assertFalse(emailValidator.isValid("@mail.com", constraintValidatorContext));
    }

    @Test
    void testValidationWithoutTailCase() {
        assertFalse(emailValidator.isValid("user1@", constraintValidatorContext));
    }

    @Test
    void testValidationNullCase() {
        assertFalse(emailValidator.isValid(null, constraintValidatorContext));
    }
}
