package elevator.system.domain.repository.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@Slf4j
public class Elevator {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    @Setter
    private Integer currentFloor;
    @Builder.Default
    private LinkedList<Integer> floorsToStop = new LinkedList<>();
    @Setter
    private Integer destinationFloor;
    @Setter
    private Integer nextFloor;
    @Setter
    @Builder.Default
    private MovingDirection movingDirection = MovingDirection.NONE;

    public boolean willElevatorStopOnFloor(Integer stopFloor, MovingDirection expectedMovingDirection) {
        if (MovingDirection.UP.equals(this.movingDirection) && MovingDirection.UP.equals(expectedMovingDirection)) {
            return this.currentFloor < stopFloor && this.destinationFloor >= stopFloor;
        } else if (MovingDirection.DOWN.equals(this.movingDirection) && MovingDirection.DOWN.equals(expectedMovingDirection)) {
            return this.currentFloor > stopFloor && this.destinationFloor <= stopFloor;
        } else {
            return this.floorsToStop.contains(stopFloor);
        }
    }

    public void addStopFloorForActiveElevator(Integer stopFloor) {
        log.info("New floor to stop for elevator {} is {}", this.id, stopFloor);
        this.floorsToStop.add(stopFloor);
        this.setNextFloorForActiveElevator();
    }

    public void setNextFloorForActiveElevator() {
        if (MovingDirection.UP.equals(this.movingDirection)) {
            this.nextFloor = this.floorsToStop.stream()
                    .filter(floor -> floor > this.currentFloor)
                    .min(Integer::compareTo)
                    .orElseThrow(() -> new RuntimeException("No destination floor found"));
            log.info("Next floor for elevator {} is {}. Stop at: {}", this.id, this.currentFloor + 1, this.nextFloor);
        } else if (MovingDirection.DOWN.equals(this.movingDirection)) {
            this.nextFloor = this.floorsToStop.stream()
                    .filter(floor -> floor < this.currentFloor)
                    .max(Integer::compareTo)
                    .orElseThrow(() -> new RuntimeException("No destination floor found"));
            log.info("Next floor for elevator {} is {}. Stop at: {}", this.id, this.currentFloor - 1, this.nextFloor);
        }
    }

    public void addDestinationFloorForInactiveElevator(Integer destinationFloor) {
        this.floorsToStop.add(destinationFloor);
        this.nextFloor = destinationFloor;
        this.destinationFloor = destinationFloor;
        this.setNewMovingDirection();
        log.info("New destination and next floor for elevator or {} is {}", this.id, this.destinationFloor);
    }

    public void setNewMovingDirection() {
        if (Objects.isNull(this.destinationFloor) || Objects.isNull(this.currentFloor)) {
            log.error("Elevator {} has no destination or current floor specified", this.id);
        } else if (this.currentFloor < this.destinationFloor) {
            log.info("Set new moving direction UP for elevator {}", this.id);
            this.movingDirection = MovingDirection.UP;
        } else if (this.currentFloor > this.destinationFloor) {
            log.info("Set new moving direction DOWN for elevator {}", this.id);
            this.movingDirection = MovingDirection.DOWN;
        }
    }

    public void moveElevatorOneFloor() {
        switch (this.movingDirection) {
            case UP -> {
                log.info("Elevator {} moving up", this.id);
                this.currentFloor = this.currentFloor + 1;
            }
            case DOWN -> {
                log.info("Elevator {} moving down", this.id);
                this.currentFloor = this.currentFloor - 1;
            }
            default -> log.info("Elevator {} is not moving", this.id);
        }
    }

    public boolean isElevatorBusy() {
        return !this.floorsToStop.isEmpty() &&
                Objects.nonNull(this.destinationFloor) &&
                Objects.nonNull(this.nextFloor) &&
                !MovingDirection.NONE.equals(this.movingDirection);
    }
}
