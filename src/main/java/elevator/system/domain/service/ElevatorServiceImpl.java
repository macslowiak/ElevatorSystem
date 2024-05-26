package elevator.system.domain.service;

import elevator.system.domain.controller.model.ElevatorCreate;
import elevator.system.domain.repository.ElevatorRepository;
import elevator.system.domain.repository.model.Elevator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;


@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class ElevatorServiceImpl implements ElevatorService {

    private final ElevatorRepository elevatorRepository;

    @Override
    public UUID createElevator(ElevatorCreate elevatorCreate) {
        final var elevator = Elevator.builder()
                .id(UUID.randomUUID())
                .currentFloor(elevatorCreate.getCurrentFloor())
                .build();
        elevatorRepository.addElevator(elevator);
        log.info("Elevator with id {} created", elevator.getId());
        return elevator.getId();
    }

    @Override
    public void deleteElevator(UUID elevatorId) {
        final var elevator = elevatorRepository.getElevators().stream()
                .filter(e -> e.getId().equals(elevatorId))
                .findFirst()
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Elevator with id " + elevatorId + " not found"));
        elevatorRepository.removeElevator(elevator);
        log.info("Elevator with id {} removed", elevatorId);
    }
}
