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
import jakarta.annotation.Nonnull;
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
    private final FlightMapper flightMapper;

    @Override
    public FlightResponse createFlight(FlightCreateRequest request, @Nonnull Long airline_id, @Nonnull Long origin_airport_id, @Nonnull Long destination_airport_id) {
        var airline = airlineRepository.findById(airline_id).orElseThrow(
                () -> new NotFoundException("Airline %d not found.".formatted(airline_id))
        );
        var origin_airport = airportRepository.findById(origin_airport_id).orElseThrow(
                () -> new NotFoundException("Airport %d not found.".formatted(origin_airport_id))
        );
        var destination_airport = airportRepository.findById(destination_airport_id).orElseThrow(
                () -> new NotFoundException("Airport %d not found.".formatted(destination_airport_id))
        );

        Flight f = flightMapper.toEntity(request);
        f.setAirline(airline);
        f.setOrigin(origin_airport);
        f.setDestination(destination_airport);

        return flightMapper.toResponse(flightRepository.save(f));
    }

    @Override @Transactional(readOnly = true)
    public FlightResponse getFlight(@Nonnull Long id) {
        return flightRepository.findById(id).map(flightMapper::toResponse).orElseThrow(
                () -> new NotFoundException("Flight %d not found.".formatted(id))
        );
    }

    @Override //A flight just can update his destination airport, I looked into it
    public FlightResponse updateFlight(FlightUpdateRequest request, @Nonnull Long id, Long destination_airport_id) {
        var flight = flightRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Flight %d not found.".formatted(id))
        );
        flightMapper.patch(request, flight);

        if (destination_airport_id != null){
            var destination = airportRepository.findById(destination_airport_id).orElseThrow(
                    () -> new NotFoundException("Airport %d not found.".formatted(destination_airport_id))
            );
            flight.setDestination(destination);
        }
        return flightMapper.toResponse(flightRepository.save(flight));
    }

    @Override
    public void deleteFlight(@Nonnull Long id) {
        flightRepository.deleteById(id);
    }

    @Override @Transactional(readOnly = true)
    public Page<FlightResponse> listScheduledFlights(Long origin_airport_id, Long destination_airport_id, @Nonnull OffsetDateTime departureFrom,
                                                     @Nonnull OffsetDateTime departureTo, Pageable pageable) {
        if (departureFrom.isAfter(departureTo)) throw new IllegalArgumentException("\"From\" date is after \"to\" date");

        var origin = (origin_airport_id != null)?  airportRepository.findById(origin_airport_id): Optional.<Airport>empty();
        var destination = (destination_airport_id != null)? airportRepository.findById(destination_airport_id) : Optional.<Airport>empty();

        var flights = (origin.isPresent() && destination.isPresent())?
                flightRepository.findByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween(origin.get().getCode(), destination.get().getCode(),
                        departureFrom, departureTo, pageable):
                flightRepository.filterByOriginAndDestinationOptionalAndDepartureTimeBetween(
                        origin.map(Airport::getCode).orElse(null), destination.map(Airport::getCode).orElse(null),
                        departureFrom, departureTo, pageable);

        return flights.map(flightMapper::toResponse);
    }

    @Override
    public FlightResponse addTagToFlight(@Nonnull Long flight_id, @Nonnull Long tag_id) {
        var flight = flightRepository.findById(flight_id).orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(flight_id)));
        var tag = tagRepository.findById(tag_id).orElseThrow(() -> new NotFoundException("Tag %d not found".formatted(tag_id)));
        flight.addTag(tag);

        return flightMapper.toResponse(flight);
    }

    @Override
    public FlightResponse removeTagFromFlight(@Nonnull Long flight_id, @Nonnull Long tag_id) {
        var flight = flightRepository.findById(flight_id).orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(flight_id)));
        var tag = tagRepository.findById(tag_id).orElseThrow(() -> new NotFoundException("Tag %d not found".formatted(tag_id)));

        flight.getTags().remove(tag);
        tag.getFlights().remove(flight);

        return flightMapper.toResponse(flight);
    }

    @Override @Transactional(readOnly = true)
    public Page<FlightResponse> listFlightsByAirline(@Nonnull Long airline_id, Pageable pageable) {
        var airline = airlineRepository.findById(airline_id).orElseThrow(() -> new NotFoundException("Airline %d not found".formatted(airline_id)));
        return flightRepository.findByAirline_Name(airline.getName(), pageable).map(flightMapper::toResponse);
    }
}
