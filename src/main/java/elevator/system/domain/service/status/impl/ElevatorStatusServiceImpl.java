package elevator.system.domain.service.status.impl;

import elevator.system.domain.repository.model.Elevator;
import elevator.system.domain.repository.ElevatorRepository;
import elevator.system.domain.repository.model.MovingDirection;
import elevator.system.domain.service.status.ElevatorStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class ElevatorStatusServiceImpl implements ElevatorStatusService {

    private final ElevatorRepository elevatorRepository;

    @Override
    public List<Elevator> getElevators() {
        return elevatorRepository.getElevators();
    }

    @Override
    public void updateElevatorStatus(UUID elevatorId, Integer destinationFloor) {
        elevatorRepository.getElevators().stream()
                .filter(elevator -> elevator.getId().equals(elevatorId))
                .findFirst()
                .filter(elevator -> !elevator.getFloorsToStop().contains(destinationFloor))
                .ifPresentOrElse(elevator -> handleUpdateElevatorStatus(elevator, destinationFloor),
                        () -> log.info("Elevator {} is already going to floor {}. Please wait.", elevatorId, destinationFloor));
    }

    private void handleUpdateElevatorStatus(Elevator elevator, Integer destinationFloor) {
        if (MovingDirection.NONE.equals(elevator.getMovingDirection())) {
            elevator.addDestinationFloorForInactiveElevator(destinationFloor);
        } else {
            elevator.addStopFloorForActiveElevator(destinationFloor);
        }
    }

}
