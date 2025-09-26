package co.edu.unimagdalena.despeganding.services.mappers;

import co.edu.unimagdalena.despeganding.api.dto.FlightDTOs.*;
import co.edu.unimagdalena.despeganding.api.dto.TagDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.Flight;
import co.edu.unimagdalena.despeganding.domain.entities.Tag;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class FlightMapper {
    //The same problem of BookingMapper xD
    public static Flight ToEntity(FlightCreateRequest request) {
        return Flight.builder().number(request.number()).build();
    }
    public static FlightResponse toDTO(Flight flight) {
        Set<TagResponse> tagResponses = flight.getTags() == null ? Collections.emptySet() :
                flight.getTags().stream()
                        .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                        .collect(Collectors.toSet());

        return  new FlightResponse(
                flight.getId(), flight.getNumber(), flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getAirline() != null ? flight.getAirline().getId() : null,
                flight.getOrigin() != null ? flight.getOrigin().getId() : null,
                flight.getDestination() != null ? flight.getDestination().getId() : null,
                tagResponses
        );
    }

    public static void path(Flight entity, FlightUpdateRequest request ) {
        if (request.number() != null ) entity.setNumber(request.number());
        if (request.departureTime() != null ) entity.setDepartureTime(request.departureTime());
        if (request.arrivalTime() != null ) entity.setArrivalTime(request.arrivalTime());
        if (request.tagsNames() != null) request.tagsNames().forEach(tagName -> entity.addTag(Tag.builder().name(tagName).build()));

    }
}
