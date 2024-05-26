package elevator.system.domain.controller;

import elevator.system.domain.repository.ElevatorRepository;
import elevator.system.domain.repository.model.Elevator;
import elevator.system.domain.repository.model.MovingDirection;
import elevator.system.openapi.model.Direction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ElevatorsOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ElevatorRepository elevatorRepository;

    @AfterEach
    void clearSystem() {
        elevatorRepository.getElevators().clear();
    }

    @Test
    void shouldOrderElevator() throws Exception {
        //given
        final var elevatorId = UUID.randomUUID();
        final var elevator = Elevator.builder()
                .id(elevatorId)
                .currentFloor(0)
                .floorsToStop(new LinkedList<>(List.of()))
                .build();
        elevatorRepository.addElevator(elevator);

        //when and then
        mockMvc.perform(post("/elevators/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"orderFloor\": %d, \"direction\": \"%s\"}", -1, Direction.DOWN)))
                .andExpect(status().isCreated());
    }

}