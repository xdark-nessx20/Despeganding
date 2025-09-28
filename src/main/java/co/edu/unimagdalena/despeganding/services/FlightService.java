package co.edu.unimagdalena.despeganding.services;

import co.edu.unimagdalena.despeganding.api.dto.FlightDTOs.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;

public interface FlightService {
    //Basic CRUD
    FlightResponse createFlight(FlightCreateRequest request, Long airline_id, Long origin_airport_id, Long destination_airport_id);
    FlightResponse getFlight(Long id);
    FlightResponse updateFlight(FlightUpdateRequest request, Long id);
    void deleteFlight(Long id);
    //--------------------------------------------------------//
    //This method looks for flights by departureTime between two dates, and also, with an origin and destination (both can be optional).
    Page<FlightResponse> listScheduledFlights(Long origin_airport_id, Long destination_airport_id, OffsetDateTime from, OffsetDateTime to, Pageable pageable);
    FlightResponse addTagToFlight(Long flight_id, Long tag_id);
    FlightResponse removeTagFromFlight(Long flight_id, Long tag_id);
    Page<FlightResponse> listFlightsByAirline(Long airline_id, Pageable pageable);
    //What name should I set up for the getAllWithExactlyTheseTags method?
}
