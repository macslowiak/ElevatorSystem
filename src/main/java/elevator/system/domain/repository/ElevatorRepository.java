package elevator.system.domain.repository;

import elevator.system.domain.repository.model.Elevator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
@Slf4j
public class ElevatorRepository {

    @Value("${elevators.number-of-elevators-limit}")
    private int numberOfElevatorsLimit;

    private final List<Elevator> elevators = Collections.synchronizedList(new ArrayList<>(numberOfElevatorsLimit));

    @Bean
    public List<Elevator> getElevators() {
        return elevators;
    }

    public void addElevator(Elevator elevator) {
        if (elevators.size() >= numberOfElevatorsLimit) {
            log.error("Cannot add more elevators. System limit of {} reached", numberOfElevatorsLimit);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Cannot add more elevators. Limit of " + numberOfElevatorsLimit + " reached");
        }
        elevators.add(elevator);
    }

    public void removeElevator(Elevator elevator) {
        if (elevator.isElevatorBusy()){
            log.error("Elevator with id {} is busy. Cannot delete it", elevator.getId());
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Elevator with id " + elevator.getId() + " is busy");
        }
        elevators.remove(elevator);
    }

    public Elevator getRandomElevator() {
        log.info("All elevators are busy. Selecting random elevator");
        final var elevatorSelected = ThreadLocalRandom.current().nextInt(elevators.size());
        return elevators.get(elevatorSelected);
    }
}
