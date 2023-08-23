package org.vitaliistf.twowheels4u.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

@Component
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {
    private String password;
    private String passwordConfirmation;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        this.password = constraintAnnotation.password();
        this.passwordConfirmation = constraintAnnotation.passwordConfirmation();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(password);
        Object fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(passwordConfirmation);

        return Objects.equals(fieldValue, fieldMatchValue);
    }
}
