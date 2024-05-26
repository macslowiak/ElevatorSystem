package elevator.system.domain.controller;

import elevator.system.domain.service.order.ElevatorOrderService;
import elevator.system.openapi.api.ElevatorsOrderApi;
import elevator.system.openapi.mapper.ElevatorMapper;
import elevator.system.openapi.model.ElevatorOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ElevatorsOrderController implements ElevatorsOrderApi {

    private final ElevatorOrderService elevatorOrderService;
    private final ElevatorMapper elevatorMapper;

    @Override
    public ResponseEntity<UUID> orderElevator(ElevatorOrderRequest elevatorOrderRequest) {
        final var elevatorOrder = elevatorMapper.elevatorOrderFrom(elevatorOrderRequest);
        final var elevatorOrdered = elevatorOrderService.orderElevator(elevatorOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(elevatorOrdered);
    }
}
