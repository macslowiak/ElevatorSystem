package elevator.system.domain.controller;

import elevator.system.domain.repository.ElevatorRepository;
import elevator.system.domain.repository.model.Elevator;
import elevator.system.domain.repository.model.MovingDirection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"building.min-floor=-1", "building.max-floor=1"})
class ElevatorsStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ElevatorRepository elevatorRepository;

    @AfterEach
    void clearSystem() {
        elevatorRepository.getElevators().clear();
    }

    @Test
    void shouldGetElevatorsStatus() throws Exception {
        //given
        final var inactiveElevator = Elevator.builder()
                .id(UUID.randomUUID())
                .currentFloor(0)
                .floorsToStop(new LinkedList<>(List.of()))
                .build();
        final var activeElevator = Elevator.builder()
                .id(UUID.randomUUID())
                .currentFloor(0)
                .destinationFloor(1)
                .nextFloor(1)
                .movingDirection(MovingDirection.UP)
                .floorsToStop(new LinkedList<>(List.of(1)))
                .build();
        var elevatorsInRepository = List.of(inactiveElevator, activeElevator);
        elevatorRepository.getElevators().addAll(elevatorsInRepository);

        //when
        final var response = mockMvc.perform(get("/elevators/status"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then
        assertThat(response)
                .isEqualToIgnoringWhitespace("""
                        [
                           {
                             "elevatorId": "%s",
                             "currentFloor": 0,
                             "destinationFloor": null,
                             "nextStopFloor": null,
                             "floorsToStop": []
                           },
                           {
                             "elevatorId": "%s",
                             "currentFloor": 0,
                             "destinationFloor": 1,
                             "nextStopFloor": 1,
                             "floorsToStop": [
                               1
                             ]
                           }
                         ]
                        """.formatted(inactiveElevator.getId(), activeElevator.getId()));
    }

    @Test
    void shouldUpdateStatusOfElevator() throws Exception {
        //given
        final var elevator = Elevator.builder()
                .id(UUID.randomUUID())
                .currentFloor(0)
                .floorsToStop(new LinkedList<>(List.of()))
                .build();
        elevatorRepository.getElevators().add(elevator);

        //when
        mockMvc.perform(put("/elevators/{elevatorId}/status", elevator.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"destinationFloor\": 1}"))
                .andExpect(status().isNoContent());

        //then
        assertThat(elevator.getDestinationFloor()).isEqualTo(1);
        assertThat(elevator.getNextFloor()).isEqualTo(1);
        assertThat(elevator.getFloorsToStop()).contains(1);
    }

}