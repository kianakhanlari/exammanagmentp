package ir.maktab.util;

import jakarta.validation.*;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtil {

    private static final ValidatorFactory validatorFactory = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();
    private static final Validator validator = validatorFactory.getValidator();

    public static <T> void validate(T t) {
        Set<ConstraintViolation<T>> violations = validator.validate(t);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(v -> "- " + v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining("\n", "Validation errors:\n", ""));
            throw new ConstraintViolationException(errorMessage, violations);
        }

    }
}

