package co.edu.unimagdalena.despeganding.api;

import co.edu.unimagdalena.despeganding.api.dto.AirportDTOs.*;
import co.edu.unimagdalena.despeganding.services.AirportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@RestController
@RequestMapping("/api/v1/airports")
@RequiredArgsConstructor
@Validated

public class AirportController {

    private final AirportService airportService;

    @PostMapping
    public ResponseEntity<AirportResponse> createAirport(@Valid @RequestBody AirportCreateRequest request,
                                                                     UriComponentsBuilder uriBuilder) {
        var body = airportService.createAirport(request);
        var location = uriBuilder.path("/api/v1/airports/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AirportResponse> getAirport(@PathVariable Long id) {
        return ResponseEntity.ok(airportService.getAirport(id));
    }
    @GetMapping(params = "code")
    public ResponseEntity<AirportResponse> getAirportByCode(@RequestParam String code) {
        return ResponseEntity.ok(airportService.getAirportByCode(code));
    }

    @GetMapping(params = "city")
    public ResponseEntity<List<AirportResponse>> getCityAirports(@RequestParam String city) {
        return ResponseEntity.ok(airportService.getCityAirports(city));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AirportResponse> updateAirport(@PathVariable Long id,
                                                                     @Valid @RequestBody AirportUpdateRequest request) {
        return ResponseEntity.ok(airportService.updateAirport(id,request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable Long id) {
        airportService.deleteAirport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<AirportResponse>> listAllAirports(Pageable pageable) {
        var result = airportService.listAllAirports(pageable);
        return ResponseEntity.ok(result);
    }
}
