package elevator.system.domain.service.moving.impl;

import elevator.system.domain.repository.ElevatorRepository;
import elevator.system.domain.repository.model.Elevator;
import elevator.system.domain.repository.model.MovingDirection;
import elevator.system.domain.service.moving.ElevatorMovingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElevatorMovingServiceImpl implements ElevatorMovingService {
    private final ElevatorRepository elevatorRepository;

    @Override
    public void moveElevator(UUID elevatorId) {
        elevatorRepository.getElevators().stream()
                .filter(elevator -> elevator.getId().equals(elevatorId))
                .findFirst()
                .ifPresent(elevator -> {
                    elevator.moveElevatorOneFloor();
                    handleElevatorStop(elevator);
                    handleElevatorArrivingAtDestinationFloor(elevator);
                    elevator.setNextFloorForActiveElevator();
                });
    }

    private void handleElevatorStop(Elevator elevator) {
        if (elevator.getCurrentFloor().equals(elevator.getNextFloor())) {
            log.info("Elevator {} arrived at floor {}. Door opened.", elevator.getId(), elevator.getCurrentFloor());
            elevator.getFloorsToStop().remove(elevator.getCurrentFloor());
        }
    }

    private void handleElevatorArrivingAtDestinationFloor(Elevator elevator) {
        if (elevator.getCurrentFloor().equals(elevator.getDestinationFloor())) {
            elevator.setMovingDirection(MovingDirection.NONE);
            setNewMainDestinationFloor(elevator);
        }
    }

    private void setNewMainDestinationFloor(Elevator elevator) {
        elevator.getFloorsToStop()
                .stream()
                .min(Integer::compareTo)
                .ifPresentOrElse(
                        mainDestinationFloor -> {
                            elevator.setDestinationFloor(mainDestinationFloor);
                            log.info("Elevator {} set new main destination floor {}", elevator.getId(), mainDestinationFloor);
                            elevator.setNewMovingDirection();
                        },
                        () -> {
                            log.info("Elevator {} has no more destination floors", elevator.getId());
                            elevator.setDestinationFloor(null);
                            elevator.setNextFloor(null);
                        }
                );
    }
}
