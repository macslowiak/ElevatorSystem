package elevator.system.domain.controller;

import elevator.system.domain.service.status.ElevatorStatusService;
import elevator.system.openapi.api.ElevatorsStatusApi;
import elevator.system.openapi.mapper.ElevatorMapper;
import elevator.system.openapi.model.ElevatorStatusResponse;
import elevator.system.openapi.model.ElevatorStatusUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ElevatorsStatusController implements ElevatorsStatusApi {

    private final ElevatorStatusService elevatorStatusService;
    private final ElevatorMapper elevatorMapper;

    @Override
    public ResponseEntity<List<ElevatorStatusResponse>> elevatorsStatus() {
        final var elevators = elevatorStatusService.getElevators();
        final var statusesOfElevators = elevatorMapper.elevatorStatusesFrom(elevators);
        return ResponseEntity.ok(statusesOfElevators);
    }

    @Override
    public ResponseEntity<Void> updateElevatorStatus(UUID elevatorId, ElevatorStatusUpdateRequest elevatorStatusUpdateRequest) {
        elevatorStatusService.updateElevatorStatus(elevatorId, elevatorStatusUpdateRequest.getDestinationFloor());
        return ResponseEntity.noContent().build();
    }
}
