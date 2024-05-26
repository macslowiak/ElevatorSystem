package elevator.system.domain.controller;

import elevator.system.domain.repository.ElevatorRepository;
import elevator.system.domain.repository.model.Elevator;
import elevator.system.domain.repository.model.MovingDirection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ElevatorsMoveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ElevatorRepository elevatorRepository;

    @AfterEach
    void clearSystem() {
        elevatorRepository.getElevators().clear();
    }

    @Test
    void shouldMoveElevator() throws Exception {
        //given
        final var elevatorId = UUID.randomUUID();
        final var elevator = Elevator.builder()
                .id(elevatorId)
                .currentFloor(0)
                .destinationFloor(1)
                .nextFloor(1)
                .movingDirection(MovingDirection.UP)
                .floorsToStop(new LinkedList<>(List.of(1)))
                .build();
        elevatorRepository.addElevator(elevator);

        //when
        mockMvc.perform(post("/elevators/{elevatorId}/move", elevatorId))
                .andExpect(status().isNoContent());

        //then
        assertThat(elevator.getCurrentFloor()).isEqualTo(1);
    }

}