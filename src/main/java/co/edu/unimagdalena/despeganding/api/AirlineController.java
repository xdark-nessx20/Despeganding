package co.edu.unimagdalena.despeganding.api;


import co.edu.unimagdalena.despeganding.api.dto.AirlineDTOs.*;
import co.edu.unimagdalena.despeganding.services.AirlineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/airlines")
@RequiredArgsConstructor
@Validated

public class AirlineController {
    private final AirlineService airlineService;

    @PostMapping
    public ResponseEntity<AirlineResponse> createAirline(@Valid @RequestBody AirlineCreateRequest request,
                                                        UriComponentsBuilder uriBuilder) {
        var body = airlineService.createAirline(request);
        var location = uriBuilder.path("/api/v1/airlines/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AirlineResponse> getAirline(@PathVariable Long id) {
        return ResponseEntity.ok(airlineService.getAirline(id));
    }
    @GetMapping(params = "code")
    public ResponseEntity<AirlineResponse> getAirlineByCode(@RequestParam String code) {
        return ResponseEntity.ok(airlineService.getAirlineByCode(code));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<AirlineResponse> updateAirline(@PathVariable Long id,
                                                         @Valid @RequestBody AirlineUpdateRequest request) {
        return ResponseEntity.ok(airlineService.updateAirline(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirline(@PathVariable Long id) {
        airlineService.deleteAirline(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AirlineResponse>> listAllAirlines() {
        return ResponseEntity.ok(airlineService.listAllAirlines());
    }

    /*@GetMapping
    public ResponseEntity<Page<AirlineResponse>> listAllAirlinesPage(Pageable pageable) {
        var result = airlineService.listAllAirlinesPage(pageable);
        return ResponseEntity.ok(result);
    }*/



}

