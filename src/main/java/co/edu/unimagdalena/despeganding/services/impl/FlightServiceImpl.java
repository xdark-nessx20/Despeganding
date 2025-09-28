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
    public FlightResponse createFlight(FlightCreateRequest request, Long airline_id, Long origin_airport_id, Long destination_airport_id) {
        var airline = airlineRepository.findById(airline_id).orElseThrow(
                () -> new NotFoundException("Airline %d not found.".formatted(airline_id))
        );
        var origin_airport = airportRepository.findById(origin_airport_id).orElseThrow(
                () -> new NotFoundException("Airport %d not found.".formatted(origin_airport_id))
        );
        var destination_airport = airportRepository.findById(destination_airport_id).orElseThrow(
                () -> new NotFoundException("Airport %d not found.".formatted(destination_airport_id))
        );

        Flight f = FlightMapper.ToEntity(request);
        f.setAirline(airline);
        f.setOrigin(origin_airport);
        f.setDestination(destination_airport);

        return FlightMapper.toResponse(f);
    }

    @Override @Transactional(readOnly = true)
    public FlightResponse getFlight(Long id) {
        return flightRepository.findById(id).map(FlightMapper::toResponse).orElseThrow(
                () -> new NotFoundException("Flight %d not found.".formatted(id))
        );
    }

    @Override //IDK if this works well or there's something left
    public FlightResponse updateFlight(FlightUpdateRequest request, Long id) {
        var flight = flightRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Flight %d not found.".formatted(id))
        );
        FlightMapper.patch(flight, request);
        return FlightMapper.toResponse(flightRepository.save(flight));
    }

    @Override
    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }

    @Override @Transactional(readOnly = true)
    public Page<FlightResponse> listScheduledFlights(Long origin_airport_id, Long destination_airport_id, OffsetDateTime from, OffsetDateTime to, Pageable pageable) {
        if (from.isAfter(to)) throw new IllegalArgumentException("\"From\" date is after \"to\" date");

        var origin =  airportRepository.findById(origin_airport_id);
        var destination =  airportRepository.findById(destination_airport_id);

        var flights = (origin.isPresent() && destination.isPresent())?
                flightRepository.findByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween(origin.get().getCode(), destination.get().getCode(), from, to, pageable):
                new PageImpl<>(
                        flightRepository.filterByOriginAndDestinationOptionalAndDepartureTimeBetween(
                                origin.map(Airport::getCode).orElse(null), destination.map(Airport::getCode).orElse(null), from, to
                        )
                );

        return flights.map(FlightMapper::toResponse);
    }

    @Override
    public FlightResponse addTagToFlight(Long flight_id, Long tag_id) {
        var flight = flightRepository.findById(flight_id).orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(flight_id)));
        var tag = tagRepository.findById(tag_id).orElseThrow(() -> new NotFoundException("Tag %d not found".formatted(tag_id)));
        FlightMapper.addTag(flight, tag);

        return FlightMapper.toResponse(flight);
    }

    @Override //Is it necessary?
    public FlightResponse removeTagFromFlight(Long flight_id, Long tag_id) {
        return null;
    }

    @Override @Transactional(readOnly = true)
    public Page<FlightResponse> listFlightsByAirline(Long airline_id, Pageable pageable) {
        var airline = airlineRepository.findById(airline_id).orElseThrow(() -> new NotFoundException("Airline %d not found".formatted(airline_id)));
        return flightRepository.findByAirline_Name(airline.getName(), pageable).map(FlightMapper::toResponse);
    }
}
