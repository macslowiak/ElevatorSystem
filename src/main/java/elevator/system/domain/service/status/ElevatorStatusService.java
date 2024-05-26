package elevator.system.domain.service.status;

import elevator.system.domain.repository.model.Elevator;

import java.util.List;
import java.util.UUID;

public interface ElevatorStatusService {
    List<Elevator> getElevators();

    void updateElevatorStatus(UUID elevatorId, Integer destinationFloor);
}
