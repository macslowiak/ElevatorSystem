package elevator.system.domain.controller.model;

import elevator.system.domain.controller.validators.ValidateFloor;
import elevator.system.domain.repository.model.MovingDirection;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ElevatorOrder {
    @ValidateFloor
    private Integer orderFloor;
    private MovingDirection direction;
}
