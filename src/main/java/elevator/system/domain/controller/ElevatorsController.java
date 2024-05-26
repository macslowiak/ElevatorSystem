package elevator.system.domain.controller;

import elevator.system.domain.service.ElevatorService;
import elevator.system.openapi.api.ElevatorsApi;
import elevator.system.openapi.mapper.ElevatorMapper;
import elevator.system.openapi.model.ElevatorCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ElevatorsController implements ElevatorsApi {

    private final ElevatorService elevatorService;
    private final ElevatorMapper elevatorMapper;


    @Override
    public ResponseEntity<UUID> createElevator(ElevatorCreateRequest elevatorCreateRequest) {
        final var elevatorCreate = elevatorMapper.elevatorCreateFrom(elevatorCreateRequest);
        final var elevatorId = elevatorService.createElevator(elevatorCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(elevatorId);
    }

    @Override
    public ResponseEntity<Void> deleteElevator(UUID elevatorId) {
        elevatorService.deleteElevator(elevatorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
