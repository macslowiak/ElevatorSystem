package elevator.system.domain.service.order.algorithms;

import elevator.system.domain.controller.model.ElevatorOrder;
import elevator.system.domain.repository.ElevatorRepository;
import elevator.system.domain.repository.model.Elevator;
import elevator.system.domain.repository.model.MovingDirection;
import elevator.system.domain.service.status.ElevatorStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DefaultElevatorOrderAlgorithmTest {

    @Mock
    private ElevatorRepository elevatorRepository;

    @Mock
    private ElevatorStatusService elevatorStatusService;

    @InjectMocks
    private DefaultElevatorOrderAlgorithm defaultElevatorOrderAlgorithm;

    @Test
    void shouldOrderElevatorFromTheSameFloor() {
        // given
        final var elevatorOrder = ElevatorOrder.builder().orderFloor(1).build();
        final var elevator = Elevator.builder().id(UUID.randomUUID()).currentFloor(1).build();

        given(elevatorRepository.getElevators()).willReturn(List.of(elevator));

        // when
        final var elevatorId = defaultElevatorOrderAlgorithm.orderElevator(elevatorOrder);

        // then
        assertThat(elevatorId).isEqualTo(elevator.getId());
    }

    @Test
    void shouldOrderElevatorWithNoDestination() {
        // given
        final var elevatorOrder = ElevatorOrder.builder().orderFloor(1).build();
        final var elevator = Elevator.builder().id(UUID.randomUUID()).currentFloor(2).build();

        given(elevatorRepository.getElevators()).willReturn(List.of(elevator));

        // when
        final var elevatorId = defaultElevatorOrderAlgorithm.orderElevator(elevatorOrder);

        // then
        assertThat(elevatorId).isEqualTo(elevator.getId());
    }

    @Test
    void shouldOrderElevatorThatWillStopOnTheFloorOrder() {
        // given
        final var elevatorOrder = ElevatorOrder.builder().orderFloor(1).build();
        final var elevator1 = Elevator.builder()
                .id(UUID.randomUUID())
                .nextFloor(1)
                .movingDirection(MovingDirection.DOWN)
                .floorsToStop(new LinkedList<>(List.of(0, 1)))
                .destinationFloor(0)
                .currentFloor(2)
                .build();
        final var elevator2 = Elevator.builder()
                .id(UUID.randomUUID())
                .nextFloor(4)
                .movingDirection(MovingDirection.UP)
                .floorsToStop(new LinkedList<>(List.of(4, 6)))
                .destinationFloor(6)
                .currentFloor(2)
                .build();

        given(elevatorRepository.getElevators()).willReturn(List.of(elevator1, elevator2));

        // when
        final var elevatorId = defaultElevatorOrderAlgorithm.orderElevator(elevatorOrder);

        // then
        assertThat(elevatorId).isEqualTo(elevator1.getId());
    }

    @Test
    void shouldOrderRandomElevatorWhenNoElevatorStopOnOrderFloor() {
        // given
        final var elevatorOrder = ElevatorOrder.builder().orderFloor(1).build();
        final var elevator1 = Elevator.builder()
                .id(UUID.randomUUID())
                .nextFloor(3)
                .movingDirection(MovingDirection.DOWN)
                .floorsToStop(new LinkedList<>(List.of(2, 3)))
                .destinationFloor(2)
                .currentFloor(4)
                .build();
        final var elevator2 = Elevator.builder()
                .id(UUID.randomUUID())
                .nextFloor(4)
                .movingDirection(MovingDirection.DOWN)
                .floorsToStop(new LinkedList<>(List.of(4, 6)))
                .destinationFloor(6)
                .currentFloor(2)
                .build();

        given(elevatorRepository.getElevators()).willReturn(List.of(elevator1, elevator2));
        given(elevatorRepository.getRandomElevator()).willReturn(elevator2);

        // when
        final var elevatorId = defaultElevatorOrderAlgorithm.orderElevator(elevatorOrder);

        // then
        assertThat(elevatorId).isEqualTo(elevator2.getId());
    }

}