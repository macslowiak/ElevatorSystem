package elevator.system.domain.repository.model;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ElevatorTest {

    @Test
    void shouldReturnTrueWhenElevatorWillNotStopOnFloorAndMovingUp() {
        //given
        final var elevator = Elevator.builder()
                .currentFloor(1)
                .destinationFloor(5)
                .movingDirection(MovingDirection.UP)
                .build();

        //when and then
        assertThat(elevator.willElevatorStopOnFloor(3, MovingDirection.UP))
                .isTrue();
    }

    @Test
    void shouldReturnTrueWhenElevatorWillNotStopOnFloorAndMovingDown() {
        //given
        final var elevator = Elevator.builder()
                .currentFloor(5)
                .destinationFloor(1)
                .movingDirection(MovingDirection.DOWN)
                .build();

        //when and then
        assertThat(elevator.willElevatorStopOnFloor(3, MovingDirection.DOWN))
                .isTrue();
    }

    @Test
    void shouldReturnFalseWhenElevatorWillNotStopOnFloorAndMovingInAnotherDirection() {
        //given
        final var elevator = Elevator.builder()
                .currentFloor(1)
                .destinationFloor(5)
                .movingDirection(MovingDirection.DOWN)
                .build();

        //when and then
        assertThat(elevator.willElevatorStopOnFloor(3, MovingDirection.UP))
                .isFalse();
    }

    @Test
    void shouldReturnTrueWhenElevatorWillStopOnTheFloorButMovingInAnotherDirection() {
        //given
        final var elevator = Elevator.builder()
                .currentFloor(1)
                .destinationFloor(5)
                .floorsToStop(new LinkedList<>(List.of(3)))
                .movingDirection(MovingDirection.UP)
                .build();

        //when and then
        assertThat(elevator.willElevatorStopOnFloor(3, MovingDirection.DOWN))
                .isTrue();
    }

    @Test
    void shouldAddStopFloorAndNextFloorForActiveElevatorWhenElevatorIsMovingThroughAddedFloor() {
        //given
        final var elevator = Elevator.builder()
                .currentFloor(5)
                .nextFloor(2)
                .movingDirection(MovingDirection.DOWN)
                .build();

        //when
        elevator.addStopFloorForActiveElevator(4);

        //then
        assertThat(elevator.getFloorsToStop()).contains(4);
        assertThat(elevator.getNextFloor()).isEqualTo(4);
    }

    @Test
    void shouldSetNextFloorForActiveElevatorWhenElevatorIsMovingUp() {
        //given
        final var elevator = Elevator.builder()
                .currentFloor(1)
                .nextFloor(5)
                .floorsToStop(new LinkedList<>(List.of(3, 5, 7)))
                .movingDirection(MovingDirection.UP)
                .build();

        //when
        elevator.setNextFloorForActiveElevator();

        //then
        assertThat(elevator.getNextFloor()).isEqualTo(3);
    }

    @Test
    void shouldSetNextFloorForActiveElevatorWhenElevatorIsMovingDown() {
        //given
        final var elevator = Elevator.builder()
                .currentFloor(7)
                .nextFloor(1)
                .floorsToStop(new LinkedList<>(List.of(3, 5, 8)))
                .movingDirection(MovingDirection.DOWN)
                .build();

        //when
        elevator.setNextFloorForActiveElevator();

        //then
        assertThat(elevator.getNextFloor()).isEqualTo(5);
    }

    @Test
    void shouldAddDestinationFloorForInactiveElevator() {
        //given
        final var elevator = Elevator.builder()
                .currentFloor(1)
                .build();

        //when
        elevator.addDestinationFloorForInactiveElevator(4);

        //then
        assertThat(elevator.getFloorsToStop()).contains(4);
        assertThat(elevator.getDestinationFloor()).isEqualTo(4);
        assertThat(elevator.getNextFloor()).isEqualTo(4);
        assertThat(elevator.getMovingDirection()).isEqualTo(MovingDirection.UP);
    }

    @Test
    void shouldSetUpMovingDirectionProperly() {
        //given
        final var elevator = Elevator.builder()
                .currentFloor(1)
                .destinationFloor(5)
                .floorsToStop(new LinkedList<>(List.of(5)))
                .build();

        //when
        elevator.setNewMovingDirection();

        //then
        assertThat(elevator.getMovingDirection()).isEqualTo(MovingDirection.UP);
    }

    @Test
    void shouldSetDownMovingDirectionProperlyWhenMovingDown() {
        //given
        final var elevator = Elevator.builder()
                .currentFloor(5)
                .destinationFloor(1)
                .floorsToStop(new LinkedList<>(List.of(1)))
                .build();

        //when
        elevator.setNewMovingDirection();

        //then
        assertThat(elevator.getMovingDirection()).isEqualTo(MovingDirection.DOWN);
    }

    @Test
    void shouldLeaveMovingDirectionAsNoneWhenNoDestinationFloor() {
        //given
        final var elevator = Elevator.builder()
                .currentFloor(5)
                .movingDirection(MovingDirection.NONE)
                .build();

        //when
        elevator.setNewMovingDirection();

        //then
        assertThat(elevator.getMovingDirection()).isEqualTo(MovingDirection.NONE);
    }

    @Test
    void shouldMoveElevatorOneFloorUp() {
        //given
        final var elevator = Elevator.builder()
                .currentFloor(5)
                .nextFloor(6)
                .movingDirection(MovingDirection.UP)
                .build();

        //when
        elevator.moveElevatorOneFloor();

        //then
        assertThat(elevator.getCurrentFloor()).isEqualTo(6);
    }

    @Test
    void shouldMoveElevatorOneFloorDown() {
        //given
        final var elevator = Elevator.builder()
                .currentFloor(5)
                .nextFloor(4)
                .movingDirection(MovingDirection.DOWN)
                .build();

        //when
        elevator.moveElevatorOneFloor();

        //then
        assertThat(elevator.getCurrentFloor()).isEqualTo(4);
    }

    @Test
    void shouldNotMoveElevatorWhenNoneMovingDirectionIsSpecified() {
        //given
        final var elevator = Elevator.builder()
                .currentFloor(5)
                .movingDirection(MovingDirection.NONE)
                .build();

        //when
        elevator.moveElevatorOneFloor();

        //then
        assertThat(elevator.getCurrentFloor()).isEqualTo(5);
    }

    @Test
    void shouldReturnTrueWhenElevatorIsBusy() {
        //given
        final var elevator = Elevator.builder()
                .floorsToStop(new LinkedList<>(List.of(3)))
                .destinationFloor(3)
                .nextFloor(3)
                .movingDirection(MovingDirection.UP)
                .build();

        //when and then
        assertThat(elevator.isElevatorBusy()).isTrue();
    }

    @Test
    void shouldReturnFalseWhenElevatorIsNotBusy() {
        //given
        final var elevator = Elevator.builder()
                .floorsToStop(new LinkedList<>(List.of()))
                .movingDirection(MovingDirection.NONE)
                .build();

        //when and then
        assertThat(elevator.isElevatorBusy()).isFalse();
    }


}