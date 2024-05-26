package elevator.system.domain.controller.validators;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = FloorValidator.class)
@TestPropertySource(properties = {"building.min-floor=-1", "building.max-floor=1"})
@ExtendWith(MockitoExtension.class)
class FloorValidatorTest {

    @Autowired
    private FloorValidator floorValidator;

    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    void shouldReturnTrueIfFloorIsValid() {
        assertTrue(floorValidator.isValid(0, constraintValidatorContext));
        assertTrue(floorValidator.isValid(-1, constraintValidatorContext));
        assertTrue(floorValidator.isValid(1, constraintValidatorContext));
    }

    @Test
    void shouldReturnFalseIfFloorIsInvalid() {
        //given
        constraintValidatorContext = Mockito.mock(ConstraintValidatorContext.class);
        final var builder = Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        Mockito.when(constraintValidatorContext.buildConstraintViolationWithTemplate(Mockito.anyString()))
                .thenReturn(builder);

        //when and then
        assertFalse(floorValidator.isValid(-2, constraintValidatorContext));
        assertFalse(floorValidator.isValid(2, constraintValidatorContext));
    }
}