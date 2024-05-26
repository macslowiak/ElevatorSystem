package elevator.system.domain.service.moving.impl;

import elevator.system.domain.repository.ElevatorRepository;
import elevator.system.domain.repository.model.Elevator;
import elevator.system.domain.repository.model.MovingDirection;
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
class ElevatorMovingServiceImplTest {

    @Mock
    private ElevatorRepository elevatorRepository;

    @InjectMocks
    private ElevatorMovingServiceImpl elevatorMovingService;

    @Test
    void shouldMoveElevatorAndStopElevatorOnThisFloor() {
        // given
        final var elevatorId = UUID.randomUUID();
        final var elevator = Elevator.builder()
                .id(elevatorId)
                .currentFloor(0)
                .nextFloor(1)
                .destinationFloor(2)
                .floorsToStop(new LinkedList<>(List.of(1, 2)))
                .movingDirection(MovingDirection.UP)
                .build();
        given(elevatorRepository.getElevators()).willReturn(List.of(elevator));

        // when
        elevatorMovingService.moveElevator(elevatorId);

        // then
        assertThat(elevator.getCurrentFloor()).isEqualTo(1);
        assertThat(elevator.getFloorsToStop()).contains(2);
        assertThat(elevator.getFloorsToStop()).hasSize(1);
    }

    @Test
    void shouldMoveElevatorAndStopOnDestinationFloorAsLastDestination() {
        // given
        final var elevatorId = UUID.randomUUID();
        final var elevator = Elevator.builder()
                .id(elevatorId)
                .currentFloor(2)
                .nextFloor(1)
                .destinationFloor(1)
                .floorsToStop(new LinkedList<>(List.of(1)))
                .movingDirection(MovingDirection.DOWN)
                .build();
        given(elevatorRepository.getElevators()).willReturn(List.of(elevator));

        // when
        elevatorMovingService.moveElevator(elevatorId);

        // then
        assertThat(elevator.getCurrentFloor()).isEqualTo(1);
        assertThat(elevator.getDestinationFloor()).isNull();
        assertThat(elevator.getNextFloor()).isNull();
        assertThat(elevator.getFloorsToStop()).isEmpty();
        assertThat(elevator.getMovingDirection()).isEqualTo(MovingDirection.NONE);
    }

    @Test
    void shouldMoveElevatorAndStopOnDestinationFloorAndSpecifyNextDestination() {
        // given
        final var elevatorId = UUID.randomUUID();
        final var elevator = Elevator.builder()
                .id(elevatorId)
                .currentFloor(2)
                .nextFloor(1)
                .destinationFloor(1)
                .floorsToStop(new LinkedList<>(List.of(1, 4)))
                .movingDirection(MovingDirection.DOWN)
                .build();
        given(elevatorRepository.getElevators()).willReturn(List.of(elevator));

        // when
        elevatorMovingService.moveElevator(elevatorId);

        // then
        assertThat(elevator.getCurrentFloor()).isEqualTo(1);
        assertThat(elevator.getDestinationFloor()).isEqualTo(4);
        assertThat(elevator.getNextFloor()).isEqualTo(4);
        assertThat(elevator.getFloorsToStop()).contains(4);
        assertThat(elevator.getFloorsToStop()).hasSize(1);
        assertThat(elevator.getMovingDirection()).isEqualTo(MovingDirection.UP);
    }

    @Test
    void shouldMoveElevatorAndDoNothing() {
        // given
        final var elevatorId = UUID.randomUUID();
        final var elevator = Elevator.builder()
                .id(elevatorId)
                .currentFloor(3)
                .nextFloor(1)
                .destinationFloor(1)
                .floorsToStop(new LinkedList<>(List.of(1)))
                .movingDirection(MovingDirection.DOWN)
                .build();
        given(elevatorRepository.getElevators()).willReturn(List.of(elevator));

        // when
        elevatorMovingService.moveElevator(elevatorId);

        // then
        assertThat(elevator.getCurrentFloor()).isEqualTo(2);
        assertThat(elevator.getDestinationFloor()).isEqualTo(1);
        assertThat(elevator.getNextFloor()).isEqualTo(1);
        assertThat(elevator.getFloorsToStop()).contains(1);
        assertThat(elevator.getMovingDirection()).isEqualTo(MovingDirection.DOWN);
    }


}