package co.edu.unimagdalena.despeganding.services.mappers;

import co.edu.unimagdalena.despeganding.api.dto.FlightDTOs.FlightResponse;
import co.edu.unimagdalena.despeganding.api.dto.TagDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.Tag;

import java.util.Set;
import java.util.stream.Collectors;

public class TagMapper {
    public static Tag toEntity(TagCreateRequest request){
        return  Tag.builder().name(request.name()).build();
    }
    public static TagResponse toResponse(Tag tag){
        return new TagResponse(
                tag.getId(), tag.getName(),
                tag.getFlights() == null ? Set.of():tag.getFlights().stream().map(flight ->
                    new FlightResponse(
                            flight.getId(),flight.getNumber(), flight.getDepartureTime(),flight.getArrivalTime(),
                            flight.getAirline() != null ? flight.getAirline().getId() : null, flight.getOrigin() != null ? flight.getOrigin().getId() : null,
                            flight.getDestination() != null ? flight.getDestination().getId() : null, null
                    )).collect(Collectors.toSet())
        );
    }
    public static void path(Tag entity, TagCreateRequest request){
        if(entity.getName() != null) entity.setName(entity.getName());
    }
}
