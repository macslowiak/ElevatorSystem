package elevator.system.domain.service.order.impl;

import elevator.system.domain.controller.model.ElevatorOrder;
import elevator.system.domain.service.order.algorithms.ElevatorOrderAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ElevatorOrderServiceImplTest {

    @Mock
    private ElevatorOrderAlgorithm elevatorOrderAlgorithm;

    @InjectMocks
    private ElevatorOrderServiceImpl elevatorOrderService;

    @Test
    void orderElevator() {
        //given
        final var elevatorOrder = ElevatorOrder.builder().build();

        //when
        elevatorOrderService.orderElevator(elevatorOrder);

        // then
        verify(elevatorOrderAlgorithm).orderElevator(elevatorOrder);
    }

}