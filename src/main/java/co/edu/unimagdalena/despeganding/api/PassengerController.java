package co.edu.unimagdalena.despeganding.api;

import co.edu.unimagdalena.despeganding.api.dto.PassengerDTOs.*;
import co.edu.unimagdalena.despeganding.services.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
@Validated

public class PassengerController {
    private final PassengerService passengerService;
    @PostMapping
    public ResponseEntity<PassengerResponse> createPassenger(@Valid@RequestBody PassengerCreateRequest request,
                                                                           UriComponentsBuilder uriBuilder) {
        var body = passengerService.createPassenger(request);
        var location = uriBuilder.path("/api/v1/passengers/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getPassenger(@PathVariable Long id) {
        return ResponseEntity.ok(passengerService.getPassenger(id));
    }
    @GetMapping("/by-email")
    public ResponseEntity<PassengerResponse> getPassengerByEmail(@RequestParam String email) {
        return ResponseEntity.ok(passengerService.getPassengerByEmail(email));
    }
    @GetMapping("/by-email-with-profile")
    public ResponseEntity<PassengerResponse> getPassengerWithProfile(@RequestParam String email) {
        return ResponseEntity.ok(passengerService.getPassengerWithProfile(email));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<PassengerResponse> updatePassenger(@PathVariable Long id,
                                                                  @Valid@RequestBody PassengerUpdateRequest request){
        return ResponseEntity.ok(passengerService.updatePassenger(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<Page<PassengerResponse>> listAllPassengers(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        var result = passengerService.listAllPassengers(PageRequest.of(page, size, Sort.by("id").ascending()));
        return ResponseEntity.ok(result);
    }
}
