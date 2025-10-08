package co.edu.unimagdalena.despeganding.api;

import co.edu.unimagdalena.despeganding.api.dto.SeatInventoryDTOs.*;
import co.edu.unimagdalena.despeganding.services.SeatInventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController @RequiredArgsConstructor @Validated
@RequestMapping("/api/v1/flights/{flightId}/seatInventories")
public class SeatInventoryController {
    private final SeatInventoryService seatService;

    @PostMapping
    public ResponseEntity<SeatInventoryResponse> create(@PathVariable Long flightId, @Valid @RequestBody
                                                        SeatInventoryCreateRequest request, UriComponentsBuilder uriBuilder) {
        var body = seatService.createSeatInventory(flightId, request);
        var location = uriBuilder.path("/api/v1/flights/{flightId}/seatInventories/{seatInventoryId}")
                .buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{seatInventoryId}")
    public ResponseEntity<SeatInventoryResponse> get(@PathVariable Long flightId, @PathVariable Long seatInventoryId) {
        return ResponseEntity.ok(seatService.getSeatInventory(seatInventoryId));
    }

    @GetMapping
    public ResponseEntity<List<SeatInventoryResponse>> listByFlight(@PathVariable Long flightId) {
        return ResponseEntity.ok(seatService.listSeatInventoriesByFlight(flightId));
    }

    @GetMapping
    public ResponseEntity<SeatInventoryResponse> getByFlightAndCabin(@PathVariable Long flightId,
                                                                     @RequestParam String cabin) {
        return ResponseEntity.ok(seatService.getSeatInventoryByFlightAndCabin(flightId, cabin));
    }

    @PatchMapping("/{seatInventoryId}")
    public ResponseEntity<SeatInventoryResponse> patch(@PathVariable Long flightId, @PathVariable Long seatInventoryId,
                                                       @Valid @RequestBody SeatInventoryUpdateRequest request) {
        return ResponseEntity.ok(seatService.updateSeatInventory(seatInventoryId, request));
    }


    @DeleteMapping("/{seatInventoryId}")
    public ResponseEntity<Void> delete(@PathVariable Long  flightId, @PathVariable Long seatInventoryId) {
        seatService.deleteSeatInventory(seatInventoryId);
        return ResponseEntity.noContent().build();
    }
}
