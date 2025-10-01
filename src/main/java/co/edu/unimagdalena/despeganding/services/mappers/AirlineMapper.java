package co.edu.unimagdalena.despeganding.services.mappers;

import co.edu.unimagdalena.despeganding.api.dto.AirlineDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.Airline;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AirlineMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flights", ignore = true)
    Airline toEntity(AirlineCreateRequest request);

    AirlineResponse toResponse(Airline airline);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flights", ignore = true)
    void patch(AirlineUpdateRequest request, @MappingTarget Airline entity);
}
