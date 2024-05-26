package elevator.system.domain.controller.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Component
@Getter
@Setter
@Slf4j
@ResponseStatus(value = BAD_REQUEST)
public class FloorValidator implements ConstraintValidator<ValidateFloor, Integer> {
    @Value("${building.max-floor}")
    private Integer maxFloor;
    @Value("${building.min-floor}")
    private Integer minFloor;

    @Override
    public void initialize(ValidateFloor constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if (integer > maxFloor || integer < minFloor) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Invalid number of floors. Must be between %d and %d".formatted(minFloor, maxFloor))
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
