package elevator.system.openapi.mapper;

import elevator.system.domain.controller.model.ElevatorCreate;
import elevator.system.domain.controller.model.ElevatorOrder;
import elevator.system.domain.repository.model.Elevator;
import elevator.system.openapi.model.ElevatorCreateRequest;
import elevator.system.openapi.model.ElevatorOrderRequest;
import elevator.system.openapi.model.ElevatorStatusResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ElevatorMapper {

    List<ElevatorStatusResponse> elevatorStatusesFrom(List<Elevator> elevator);

    ElevatorOrder elevatorOrderFrom(ElevatorOrderRequest orderRequest);

    @Mapping(target = "elevatorId", source = "id")
    @Mapping(target = "nextStopFloor", source = "nextFloor")
    ElevatorStatusResponse elevatorStatusFrom(Elevator elevator);

    @Mapping(target = "currentFloor", source = "currentElevatorFloor")
    ElevatorCreate elevatorCreateFrom(ElevatorCreateRequest elevatorCreateRequest);


}
