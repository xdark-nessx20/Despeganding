package co.edu.unimagdalena.despeganding.services;

import co.edu.unimagdalena.despeganding.api.dto.FlightDTOs.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;

public interface FlightService {
    //Basic CRUD
    FlightResponse createFlight(FlightCreateRequest request);
    FlightResponse getFlight(Long id);
    FlightResponse updateFlight(Long id, FlightUpdateRequest request);
    void deleteFlight(Long id);
    //--------------------------------------------------------//
    //This method looks for flights by departureTime between two dates, and also, with an origin and destination (both can be optional).
    Page<FlightResponse> listScheduledFlights(String origin_airport_code, String destination_airport_code,
                                              OffsetDateTime from, OffsetDateTime to, Pageable pageable);
    FlightResponse addTagToFlight(Long flight_id, Long tag_id);
    FlightResponse removeTagFromFlight(Long flight_id, Long tag_id);
    Page<FlightResponse> listFlightsByAirline(String airline_name, Pageable pageable);
    //What name should I set up for the getAllWithExactlyTheseTags method?
}
