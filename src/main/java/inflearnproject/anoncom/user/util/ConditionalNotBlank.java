package inflearnproject.anoncom.user.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ConditionalNotBlankValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionalNotBlank {
    String message() default "The field must be either blank or between 8 and 20 characters in length";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}