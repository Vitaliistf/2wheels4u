package org.vitaliistf.twowheels4u.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Password should have minimum 8 characters in length and contain "
            + "at least one uppercase English letter, at least one lowercase English letter and "
            + "at least one digit.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
