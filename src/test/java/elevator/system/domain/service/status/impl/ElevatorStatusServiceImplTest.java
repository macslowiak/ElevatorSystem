package elevator.system.domain.service.status.impl;

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
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ElevatorStatusServiceImplTest {

    @Mock
    private ElevatorRepository elevatorRepository;

    @InjectMocks
    private ElevatorStatusServiceImpl elevatorStatusService;

    @Test
    void shouldGetElevators() {
        //when
        elevatorStatusService.getElevators();

        //then
        verify(elevatorRepository).getElevators();
    }

    @Test
    void shouldUpdateElevatorStatusForActiveElevator() {
        //given
        final var destinationFloor = 5;
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

        //when
        elevatorStatusService.updateElevatorStatus(elevatorId, destinationFloor);

        //then
        assertThat(elevator.getFloorsToStop()).contains(destinationFloor);
    }

    @Test
    void shouldNotUpdateElevatorStatusWhenElevatorIsAlreadyGoingToThatFloor() {
        //given
        final var destinationFloor = 2;
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

        //when
        elevatorStatusService.updateElevatorStatus(elevatorId, destinationFloor);

        //then
        assertThat(elevator.getFloorsToStop()).contains(destinationFloor);
        assertThat(elevator.getFloorsToStop()).hasSize(2);
    }

    @Test
    void shouldUpdateElevatorStatusForInactiveElevator() {
        //given
        final var destinationFloor = 2;
        final var elevatorId = UUID.randomUUID();
        final var elevator = Elevator.builder()
                .id(elevatorId)
                .currentFloor(1)
                .floorsToStop(new LinkedList<>(List.of()))
                .movingDirection(MovingDirection.NONE)
                .build();

        given(elevatorRepository.getElevators()).willReturn(List.of(elevator));

        //when
        elevatorStatusService.updateElevatorStatus(elevatorId, destinationFloor);

        //then
        assertThat(elevator.getFloorsToStop()).contains(destinationFloor);
        assertThat(elevator.getNextFloor()).isEqualTo(destinationFloor);
        assertThat(elevator.getDestinationFloor()).isEqualTo(destinationFloor);
        assertThat(elevator.getMovingDirection()).isEqualTo(MovingDirection.UP);
    }

}