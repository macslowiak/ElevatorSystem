package elevator.system.domain.service.order;


import elevator.system.domain.controller.model.ElevatorOrder;
import jakarta.validation.Valid;

import java.util.UUID;

public interface ElevatorOrderService {
    UUID orderElevator(@Valid ElevatorOrder elevatorOrder);

}
