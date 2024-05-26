package elevator.system.domain.service.order.impl;

import elevator.system.domain.controller.model.ElevatorOrder;
import elevator.system.domain.service.order.ElevatorOrderService;
import elevator.system.domain.service.order.algorithms.ElevatorOrderAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class ElevatorOrderServiceImpl implements ElevatorOrderService {

    private final ElevatorOrderAlgorithm elevatorOrderAlgorithm;

    @Override
    public UUID orderElevator(ElevatorOrder elevatorOrder) {
        return elevatorOrderAlgorithm.orderElevator(elevatorOrder);
    }
}
