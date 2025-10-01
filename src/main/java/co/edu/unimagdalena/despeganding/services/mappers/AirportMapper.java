package co.edu.unimagdalena.despeganding.services.mappers;

import co.edu.unimagdalena.despeganding.api.dto.AirportDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.Airport;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AirportMapper {
    @Mapping(target = "id", ignore = true)
    Airport toEntity(AirportCreateRequest request);

    AirportResponse toResponse(Airport airport);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "city", ignore = true)
    void patch(AirportUpdateRequest request, @MappingTarget Airport airport);
}