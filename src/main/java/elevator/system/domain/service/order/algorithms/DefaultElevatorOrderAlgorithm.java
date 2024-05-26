package elevator.system.domain.service.order.algorithms;

import elevator.system.domain.controller.model.ElevatorOrder;
import elevator.system.domain.repository.ElevatorRepository;
import elevator.system.domain.repository.model.Elevator;
import elevator.system.domain.service.status.ElevatorStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultElevatorOrderAlgorithm implements ElevatorOrderAlgorithm {

    private final ElevatorRepository elevatorRepository;
    private final ElevatorStatusService elevatorStatusService;

    @Override
    public UUID orderElevator(ElevatorOrder elevatorOrder) {
        final var elevatorOnTheSameFloor = getElevatorFromTheSameFloor(elevatorOrder);
        if (elevatorOnTheSameFloor.isPresent()) {
            log.info("Elevator {} is available on the same floor ({}). Door opened.",
                    elevatorOnTheSameFloor.get().getId(), elevatorOrder.getOrderFloor());
            return elevatorOnTheSameFloor.get().getId();
        } else {
            final var orderedElevator = getElevatorWithNoDestination(elevatorOrder)
                    .orElseGet(() -> getElevatorWhenAllElevatorsAreBusy(elevatorOrder));
            elevatorStatusService.updateElevatorStatus(orderedElevator.getId(), elevatorOrder.getOrderFloor());
            return orderedElevator.getId();
        }
    }

    private Optional<Elevator> getElevatorFromTheSameFloor(ElevatorOrder elevatorOrder) {
        return elevatorRepository.getElevators()
                .stream()
                .filter(elevator -> elevator.getCurrentFloor().equals(elevatorOrder.getOrderFloor()))
                .findAny();
    }

    private Optional<Elevator> getElevatorWithNoDestination(ElevatorOrder elevatorOrder) {
        return elevatorRepository.getElevators()
                .stream()
                .filter(elevator -> elevator.getDestinationFloor() == null)
                .min(compareElevatorDistanceToOrderFloor(elevatorOrder));
    }

    private Comparator<Elevator> compareElevatorDistanceToOrderFloor(ElevatorOrder elevatorOrder) {
        return Comparator.comparingInt(elevator -> Math.abs(elevator.getCurrentFloor() - elevatorOrder.getOrderFloor()));
    }

    private Elevator getElevatorWhenAllElevatorsAreBusy(ElevatorOrder elevatorOrder) {
        final var elevatorToOrder = elevatorRepository.getElevators().stream()
                .filter(elevator -> elevator.willElevatorStopOnFloor(elevatorOrder.getOrderFloor(), elevatorOrder.getDirection()))
                .findFirst()
                .orElseGet(elevatorRepository::getRandomElevator);
        log.info("Ordering elevator {} to floor {}. Currently it is on floor {} and its main destination floor is {}",
                elevatorToOrder.getId(), elevatorOrder.getOrderFloor(), elevatorToOrder.getCurrentFloor(), elevatorToOrder.getDestinationFloor());
        return elevatorToOrder;
    }
}
