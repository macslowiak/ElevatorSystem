package elevator.system.domain.controller.simulation;

import elevator.system.domain.repository.ElevatorRepository;
import elevator.system.domain.repository.model.Elevator;
import elevator.system.openapi.model.Direction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class SimulationTest {

    private ElevatorsApiRestTestInvoker elevatorsApiRestTestInvoker = new ElevatorsApiRestTestInvoker();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ElevatorRepository elevatorRepository;

    @AfterEach
    void clearSystem() {
        elevatorRepository.getElevators().clear();
    }


    @Test
    void exampleOfSimulation() throws Exception {
        elevatorsApiRestTestInvoker.createElevator(mockMvc, 1);
        elevatorsApiRestTestInvoker.createElevator(mockMvc, 2);
        elevatorsApiRestTestInvoker.createElevator(mockMvc, 3);
        List<UUID> elevatorsUuids = elevatorRepository.getElevators().stream().map(Elevator::getId).toList();
        elevatorsApiRestTestInvoker.orderElevator(mockMvc, 7, Direction.DOWN);
        elevatorsApiRestTestInvoker.orderElevator(mockMvc, 8, Direction.DOWN);
        elevatorsApiRestTestInvoker.orderElevator(mockMvc, 1, Direction.UP);
        elevatorsApiRestTestInvoker.moveElevators(mockMvc, elevatorsUuids, 1);
    }

}
