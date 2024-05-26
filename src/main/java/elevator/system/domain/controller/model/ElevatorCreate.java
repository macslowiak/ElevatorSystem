package elevator.system.domain.controller.model;

import elevator.system.domain.controller.validators.ValidateFloor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ElevatorCreate {
    @ValidateFloor
    private Integer currentFloor;
}
