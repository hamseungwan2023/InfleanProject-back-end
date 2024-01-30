package inflearnproject.anoncom.user.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConditionalNotBlankValidator implements ConstraintValidator<ConditionalNotBlank, String> {
    private int min;
    private int max;

    @Override
    public void initialize(ConditionalNotBlank constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null을 허용합니다.
        }
        value = value.trim(); // 앞뒤 공백 제거
        return value.isEmpty() || (value.length() >= min && value.length() <= max);
    }
}