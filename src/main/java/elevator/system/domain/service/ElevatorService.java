package elevator.system.domain.service;

import elevator.system.domain.controller.model.ElevatorCreate;
import jakarta.validation.Valid;

import java.util.UUID;

public interface ElevatorService {
    UUID createElevator(@Valid ElevatorCreate elevatorCreate);
    void deleteElevator(UUID elevatorId);
}
