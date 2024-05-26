package elevator.system.domain.service;

import elevator.system.domain.controller.model.ElevatorCreate;
import elevator.system.domain.repository.ElevatorRepository;
import elevator.system.domain.repository.model.Elevator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ElevatorServiceImplTest {

    @Mock
    private ElevatorRepository elevatorRepository;

    @InjectMocks
    private ElevatorServiceImpl elevatorService;

    @Test
    void shouldCreateElevator() {
        // given
        final var elevatorCreate = ElevatorCreate.builder()
                .currentFloor(0)
                .build();

        doNothing().when(elevatorRepository).addElevator(any(Elevator.class));

        // when
        final var elevatorId = elevatorService.createElevator(elevatorCreate);

        // then
        assertThat(elevatorId).isNotNull();
    }

    @Test
    void shouldDeleteElevator() {
        // given
        final var elevatorId = UUID.randomUUID();
        final var elevator = Elevator.builder().id(elevatorId).build();

        given(elevatorRepository.getElevators()).willReturn(List.of(elevator));

        // when
        elevatorService.deleteElevator(elevatorId);

        // then
        verify(elevatorRepository).removeElevator(elevator);
    }

    @Test
    void shouldThrowResponseStatusWithNotFoundExceptionWhenCouldNotFindElevatorToDeleteByUuid() {
        // given
        final var elevatorId = UUID.randomUUID();
        final var elevator = Elevator.builder().id(UUID.randomUUID()).build();

        given(elevatorRepository.getElevators()).willReturn(List.of(elevator));

        // when and then
        assertThatThrownBy(() -> elevatorService.deleteElevator(elevatorId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("404 NOT_FOUND \"Elevator with id " + elevatorId + " not found\"");

    }
}