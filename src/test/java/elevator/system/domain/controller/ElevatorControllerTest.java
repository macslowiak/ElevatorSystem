package elevator.system.domain.controller;

import elevator.system.domain.repository.ElevatorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"building.min-floor=-1", "building.max-floor=1"})
class ElevatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ElevatorRepository elevatorRepository;

    @AfterEach
    void clearSystem() {
        elevatorRepository.getElevators().clear();
    }

    @Test
    void shouldCreateElevator() throws Exception {
        //given and when
        createElevator(0);
        final var elevators = elevatorRepository.getElevators();

        //then
        assertThat(elevators).hasSize(1);
    }

    @Test
    void shouldThrowValidationExceptionWhenProvideWrongFloor() throws Exception {
        mockMvc.perform(post("/elevators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"currentElevatorFloor\": %d}", 4)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteElevator() throws Exception {
        //given
        createElevator(0);
        final var elevators = elevatorRepository.getElevators();
        assertThat(elevators).hasSize(1);

        //when
        deleteElevator(elevators.getFirst().getId());
        final var elevatorAfterDelete = elevatorRepository.getElevators();

        //then
        assertThat(elevatorAfterDelete).isEmpty();
    }

    private void createElevator(Integer currentElevatorFloor) throws Exception {
        mockMvc.perform(post("/elevators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"currentElevatorFloor\": %d}", currentElevatorFloor)))
                .andExpect(status().isCreated());
    }

    private void deleteElevator(UUID elevatorId) throws Exception {
        mockMvc.perform(delete("/elevators/{elevatorId}", elevatorId))
                .andExpect(status().isNoContent());
    }

}