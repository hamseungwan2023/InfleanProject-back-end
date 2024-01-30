package inflearnproject.anoncom.user.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConditionalNotBlankValidator implements ConstraintValidator<ConditionalNotBlank, String> {

    @Override
    public void initialize(ConditionalNotBlank constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null을 허용합니다.
        }
        value = value.trim(); // 앞뒤 공백 제거
        return value.isEmpty() || (value.length() >= 8 && value.length() <= 20);
    }
}