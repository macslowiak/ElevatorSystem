package elevator.system.domain.controller.simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import elevator.system.openapi.model.Direction;
import elevator.system.openapi.model.ElevatorStatusResponse;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ElevatorsApiRestTestInvoker {

    public void createElevator(MockMvc mockMvc, Integer currentElevatorFloor) throws Exception {
        mockMvc.perform(post("/elevators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"currentElevatorFloor\": %d}", currentElevatorFloor)))
                .andExpect(status().isCreated());
    }

    public void deleteElevator(MockMvc mockMvc, UUID elevatorId) throws Exception {
        mockMvc.perform(delete("/elevators/{elevatorId}", elevatorId))
                .andExpect(status().isNoContent());
    }

    public void orderElevator(MockMvc mockMvc, Integer orderFloor, Direction direction) throws Exception {
        mockMvc.perform(post("/elevators/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"orderFloor\": %d, \"direction\": \"%s\"}", orderFloor, direction)))
                .andExpect(status().isCreated());
    }

    public void getElevatorsStatus(MockMvc mockMvc) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        final var response = mockMvc.perform(get("/elevators/status"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ElevatorStatusResponse> elevatorStatuses = objectMapper
                .readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, ElevatorStatusResponse.class));

        System.out.println("Elevator system status: ");
        elevatorStatuses.forEach(System.out::println);
    }

    public void updateStatusOfElevator(MockMvc mockMvc, UUID elevatorId, Integer destinationFloor) throws Exception {
        mockMvc.perform(put("/elevators/{elevatorId}/status", elevatorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"destinationFloor\": %d}", destinationFloor)))
                .andExpect(status().isNoContent());
    }

    public void moveElevators(MockMvc mockMvc, List<UUID> elevatorsUuids, int times) throws Exception {
        for (int i = 0; i < times; i++) {
            System.out.println("Step: " + i);
            for (UUID elevatorId : elevatorsUuids) {
                mockMvc.perform(post("/elevators/{elevatorId}/move", elevatorId))
                        .andExpect(status().isNoContent());
            }
        }
    }

}
