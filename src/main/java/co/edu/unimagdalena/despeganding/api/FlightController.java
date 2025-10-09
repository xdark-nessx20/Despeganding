package co.edu.unimagdalena.despeganding.api;

import co.edu.unimagdalena.despeganding.api.dto.FlightDTOs.*;
import co.edu.unimagdalena.despeganding.services.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.OffsetDateTime;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/flights")
public class FlightController {
    private final FlightService flightService;

    @PostMapping
    public ResponseEntity<FlightResponse> create(@Valid @RequestBody FlightCreateRequest request,
                                                 UriComponentsBuilder uriBuilder) {
        var body = flightService.createFlight(request);
        var location = uriBuilder.path("/api/v1/flights/{flightId}")
                .buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{flightId}")
    public ResponseEntity<FlightResponse> get(@PathVariable Long flightId) {
        return ResponseEntity.ok(flightService.getFlight(flightId));
    }

    @PatchMapping("/{flightId}")
    public ResponseEntity<FlightResponse> patch(@PathVariable Long flightId, @Valid @RequestBody FlightUpdateRequest request) {
        return ResponseEntity.ok(flightService.updateFlight(flightId, request));
    }

    @DeleteMapping("/{flightId}")
    public ResponseEntity<Void> delete(@PathVariable Long flightId) {
        flightService.deleteFlight(flightId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{flightId}/tags/{tagId}")
    public ResponseEntity<FlightResponse> addTag(@PathVariable Long flightId, @PathVariable Long tagId) {
        return ResponseEntity.ok(flightService.addTagToFlight(flightId, tagId));
    }

    @DeleteMapping("/{flightId}/tags/{tagId}")
    public ResponseEntity<FlightResponse> deleteTag(@PathVariable Long flightId, @PathVariable Long tagId) {
        return ResponseEntity.ok(flightService.removeTagFromFlight(flightId, tagId));
    }

    //params is for avoid duplicate endpoints
    @GetMapping(params = "airlineName")
    public ResponseEntity<Page<FlightResponse>> getByAirline(@RequestParam String airlineName, Pageable pageable) {
        var flight_page = flightService.listFlightsByAirline(airlineName, pageable);
        return ResponseEntity.ok(flight_page);
    }

    @GetMapping(params = {"departureTime", "arrivalTime"})
    public ResponseEntity<Page<FlightResponse>> listByAirportsAndTime(@RequestParam(required = false) String originAirportCode, @RequestParam(required = false) String destinationAirportCode,
                                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime departureTime,
                                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime arrivalTime,
                                                                      Pageable pageable) {
        var flight_page = flightService.listScheduledFlights(originAirportCode, destinationAirportCode, departureTime, arrivalTime, pageable);
        return ResponseEntity.ok(flight_page);
    }
}
