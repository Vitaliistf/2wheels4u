package org.vitaliistf.twowheels4u.util;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vitaliistf.twowheels4u.util.validation.PasswordValidator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {
    private static final String PASSWORD = "123QWEasd";
    @InjectMocks
    private PasswordValidator passwordValidator;
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;


    @Test
    void testValidationNormalCase() {
        assertTrue(passwordValidator.isValid(PASSWORD, constraintValidatorContext));
    }

    @Test
    void testValidationOnlyNumbersCase() {
        assertFalse(passwordValidator.isValid("11111111", constraintValidatorContext));
    }

    @Test
    void testValidationWithoutUpperLettersCase() {
        assertFalse(passwordValidator.isValid("1111abcd", constraintValidatorContext));
    }

    @Test
    void testValidationWithoutLowerLettersCase() {
        assertFalse(passwordValidator.isValid("1111ABCD", constraintValidatorContext));
    }

    @Test
    void testValidationWithoutNumbersCase() {
        assertFalse(passwordValidator.isValid("abcdABCD", constraintValidatorContext));
    }

    @Test
    void testValidationNullCase() {
        assertFalse(passwordValidator.isValid(null, constraintValidatorContext));
    }
}
