package elevator.system.domain.service.order.algorithms;


import elevator.system.domain.controller.model.ElevatorOrder;

import java.util.UUID;

public interface ElevatorOrderAlgorithm {
    UUID orderElevator(ElevatorOrder elevatorOrder);
}
