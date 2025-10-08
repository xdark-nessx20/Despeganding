package co.edu.unimagdalena.despeganding.services.impl;

import co.edu.unimagdalena.despeganding.domain.entities.Airport;
import co.edu.unimagdalena.despeganding.domain.entities.Flight;
import co.edu.unimagdalena.despeganding.domain.repositories.AirlineRepository;
import co.edu.unimagdalena.despeganding.domain.repositories.AirportRepository;
import co.edu.unimagdalena.despeganding.domain.repositories.FlightRepository;
import co.edu.unimagdalena.despeganding.domain.repositories.TagRepository;
import co.edu.unimagdalena.despeganding.exceptions.NotFoundException;
import co.edu.unimagdalena.despeganding.services.FlightService;
import co.edu.unimagdalena.despeganding.services.mappers.FlightMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;
import java.util.Optional;

import co.edu.unimagdalena.despeganding.api.dto.FlightDTOs.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Transactional
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;
    private final AirlineRepository airlineRepository;
    private final AirportRepository airportRepository;
    private final TagRepository tagRepository;

    @Override
    public FlightResponse createFlight(FlightCreateRequest request) {
        var airline = airlineRepository.findById(request.airline_id()).orElseThrow(
                () -> new NotFoundException("Airline %d not found.".formatted(request.airline_id()))
        );
        var origin_airport = airportRepository.findById(request.origin_airport_id()).orElseThrow(
                () -> new NotFoundException("Airport %d not found.".formatted(request.origin_airport_id()))
        );
        var destination_airport = airportRepository.findById(request.destination_airport_id()).orElseThrow(
                () -> new NotFoundException("Airport %d not found.".formatted(request.destination_airport_id()))
        );

        Flight f = FlightMapper.ToEntity(request);
        f.setAirline(airline);
        f.setOrigin(origin_airport);
        f.setDestination(destination_airport);

        return FlightMapper.toResponse(flightRepository.save(f));
    }

    @Override @Transactional(readOnly = true)
    public FlightResponse getFlight(Long id) {
        return flightRepository.findById(id).map(FlightMapper::toResponse).orElseThrow(
                () -> new NotFoundException("Flight %d not found.".formatted(id))
        );
    }

    @Override //A flight just can update his destination airport, I looked into it
    public FlightResponse updateFlight(Long id, FlightUpdateRequest request) {
        var flight = flightRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Flight %d not found.".formatted(id))
        );
        FlightMapper.patch(flight, request);

        if (request.destination_airport_id() != null){
            var destination = airportRepository.findById(request.destination_airport_id()).orElseThrow(
                    () -> new NotFoundException("Airport %d not found.".formatted(request.destination_airport_id()))
            );
            flight.setDestination(destination);
        }
        return FlightMapper.toResponse(flightRepository.save(flight));
    }

    @Override
    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }

    @Override @Transactional(readOnly = true)
    public Page<FlightResponse> listScheduledFlights(String origin_airport_code, String destination_airport_code,
                                                     OffsetDateTime from, OffsetDateTime to, Pageable pageable) {
        if (from.isAfter(to)) throw new IllegalArgumentException("\"From\" date is after \"to\" date");

        var origin = (origin_airport_code == null || origin_airport_code.isBlank())? Optional.<Airport>empty()
                :airportRepository.findByCode(origin_airport_code);
        var destination = (destination_airport_code == null || destination_airport_code.isBlank())? Optional.<Airport>empty()
                :airportRepository.findByCode(destination_airport_code);

        var flights = (origin.isPresent() && destination.isPresent())?
                flightRepository.findByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween(origin.get().getCode(), destination.get().getCode(), from, to, pageable):
                new PageImpl<>(
                        flightRepository.filterByOriginAndDestinationOptionalAndDepartureTimeBetween(
                                origin.map(Airport::getCode).orElse(null), destination.map(Airport::getCode).orElse(null), from, to
                        )
                ); //There's no pageable, should I put in there?

        return flights.map(FlightMapper::toResponse);
    }

    @Override
    public FlightResponse addTagToFlight(Long flight_id, Long tag_id) {
        var flight = flightRepository.findById(flight_id).orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(flight_id)));
        var tag = tagRepository.findById(tag_id).orElseThrow(() -> new NotFoundException("Tag %d not found".formatted(tag_id)));
        FlightMapper.addTag(flight, tag);

        return FlightMapper.toResponse(flight);
    }

    @Override
    public FlightResponse removeTagFromFlight(Long flight_id, Long tag_id) {
        var flight = flightRepository.findById(flight_id).orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(flight_id)));
        var tag = tagRepository.findById(tag_id).orElseThrow(() -> new NotFoundException("Tag %d not found".formatted(tag_id)));

        flight.getTags().remove(tag);
        tag.getFlights().remove(flight);

        return FlightMapper.toResponse(flight);
    }

    @Override @Transactional(readOnly = true)
    public Page<FlightResponse> listFlightsByAirline(String airline_name, Pageable pageable) {
        var airline = airlineRepository.findByName(airline_name).orElseThrow(
                () -> new NotFoundException("Airline %s not found".formatted(airline_name))
        );
        return flightRepository.findByAirline_Name(airline.getName(), pageable).map(FlightMapper::toResponse);
    }
}
