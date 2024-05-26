package elevator.system.domain.controller;

import elevator.system.domain.service.moving.ElevatorMovingService;
import elevator.system.openapi.api.ElevatorsMoveApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ElevatorsMoveController implements ElevatorsMoveApi {

    private final ElevatorMovingService elevatorMovingService;

    @Override
    public ResponseEntity<Void> moveElevator(UUID elevatorId) {
        elevatorMovingService.moveElevator(elevatorId);
        return ResponseEntity.noContent().build();
    }
}
