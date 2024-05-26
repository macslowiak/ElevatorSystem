package elevator.system.domain.repository;

import elevator.system.domain.repository.model.Elevator;
import elevator.system.domain.repository.model.MovingDirection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {ElevatorRepository.class})
@TestPropertySource(properties = {"elevators.number-of-elevators-limit=3"})
class ElevatorRepositoryTest {

    @Autowired
    private ElevatorRepository elevatorRepository;

    @AfterEach
    void deleteElevators() {
        elevatorRepository.getElevators().clear();
    }

    @Test
    void shouldAddElevatorWhenLimitIsNotReached() {
        //given
        final var elevator = Elevator.builder().id(UUID.randomUUID()).build();
        elevatorRepository.addElevator(elevator);

        //when and then
        assertThat(elevatorRepository.getElevators()).isNotEmpty();
    }

    @Test
    void shouldNotAddElevatorWhenLimitIsReached() {
        //given
        for (int i = 0; i < 3; i++) {
            final var elevator = Elevator.builder().id(UUID.randomUUID()).build();
            elevatorRepository.addElevator(elevator);
        }
        final var elevatorWhichReachLimit = Elevator.builder().id(UUID.randomUUID()).build();

        //when and then
        assertThatThrownBy(() -> elevatorRepository.addElevator(elevatorWhichReachLimit))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("500 INTERNAL_SERVER_ERROR \"Cannot add more elevators. Limit of 3 reached\"");
    }

    @Test
    void shouldRemoveElevatorWhenElevatorIsNotBusy() {
        //given
        final var elevator = Elevator.builder().id(UUID.randomUUID()).build();
        elevatorRepository.addElevator(elevator);

        //when
        elevatorRepository.removeElevator(elevator);

        //then
        assertThat(elevatorRepository.getElevators()).isEmpty();
    }

    @Test
    void shouldNotRemoveElevatorWhenElevatorIsBusy() {
        //given
        final var elevator = Elevator.builder()
                .id(UUID.randomUUID())
                .nextFloor(1)
                .destinationFloor(1)
                .currentFloor(2)
                .movingDirection(MovingDirection.DOWN)
                .floorsToStop(new LinkedList<>(List.of(1)))
                .build();
        elevatorRepository.addElevator(elevator);

        //when and then
        assertThatThrownBy(() -> elevatorRepository.removeElevator(elevator))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("500 INTERNAL_SERVER_ERROR \"Elevator with id " + elevator.getId() + " is busy\"");
    }

    @Test
    void shouldGetRandomElevator() {
        //given
        final var elevator1 = Elevator.builder().id(UUID.randomUUID()).build();
        final var elevator2 = Elevator.builder().id(UUID.randomUUID()).build();
        final var elevator3 = Elevator.builder().id(UUID.randomUUID()).build();
        elevatorRepository.addElevator(elevator1);
        elevatorRepository.addElevator(elevator2);
        elevatorRepository.addElevator(elevator3);

        //when
        final var randomElevator = elevatorRepository.getRandomElevator();

        //then
        assertThat(randomElevator).isIn(elevator1, elevator2, elevator3);
    }

}