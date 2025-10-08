package co.edu.unimagdalena.despeganding.services.mappers;

import co.edu.unimagdalena.despeganding.api.dto.FlightDTOs.*;
import co.edu.unimagdalena.despeganding.api.dto.TagDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.Flight;
import co.edu.unimagdalena.despeganding.domain.entities.Tag;

import java.util.Set;
import java.util.stream.Collectors;

public class FlightMapper {
    public static Flight ToEntity(FlightCreateRequest request) {
        return Flight.builder().number(request.number()).arrivalTime(request.arrivalTime())
                .departureTime(request.departureTime()).build();
    }

    public static FlightResponse toResponse(Flight flight) {
        Set<TagResponse> tagResponses = flight.getTags() == null ? Set.of() :
                flight.getTags().stream()
                        .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                        .collect(Collectors.toSet());
        var airline = flight.getAirline();
        var origin = flight.getOrigin();
        var destination = flight.getDestination();

        return  new FlightResponse(flight.getId(), flight.getNumber(), flight.getDepartureTime(),
                flight.getArrivalTime(), airline.getId(), airline.getName(), origin.getId(),
                origin.getName(), destination.getId(), destination.getName(), tagResponses
        );
    }

    public static void patch(Flight entity, FlightUpdateRequest request ) {
        if (request.number() != null ) entity.setNumber(request.number());
        if (request.departureTime() != null ) entity.setDepartureTime(request.departureTime());
        if (request.arrivalTime() != null ) entity.setArrivalTime(request.arrivalTime());
    }

    public static void addTag(Flight flight, Tag tag) {
        flight.addTag(tag);
    }
}
