package co.edu.unimagdalena.despeganding.services.mappers;

import co.edu.unimagdalena.despeganding.api.dto.SeatInventoryDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.SeatInventory;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SeatInventoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flight", ignore = true)
    @Mapping(source = "cabin", target = "cabin")
    SeatInventory toEntity(SeatInventoryCreateRequest request);

    @Mapping(source = "flight.id", target = "flight_id")
    @Mapping(source = "cabin", target = "cabin")
    SeatInventoryResponse toResponse(SeatInventory seatInventory);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flight", ignore = true)
    void patch(SeatInventoryUpdateRequest request, @MappingTarget SeatInventory entity);
}