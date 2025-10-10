package co.edu.unimagdalena.despeganding.api;

import co.edu.unimagdalena.despeganding.api.dto.AirportDTOs;
import co.edu.unimagdalena.despeganding.api.dto.BookingDTOs.*;
import co.edu.unimagdalena.despeganding.services.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.OffsetDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Validated

public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingCreateRequest request,
                                                                     UriComponentsBuilder uriBuilder) {
        var body = bookingService.createBooking(request);
        var location = uriBuilder.path("/api/v1/bookings/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBooking(id));
    }

    @GetMapping("/between-dates")
    public ResponseEntity<List<BookingResponse>> listBookingsBetweenDates(@RequestParam OffsetDateTime start,
                                                                          @RequestParam OffsetDateTime end) {
        return ResponseEntity.ok(bookingService.listBookingsBetweenDates(start, end));
    }

    @GetMapping("/by-email")
    public ResponseEntity<Page<BookingResponse>> listBookingsByPassengerEmail(@RequestParam String email,
                                                                              Pageable pageable) {
        return ResponseEntity.ok(bookingService.listBookingsByPassengerEmailAndOrderedMostRecently(email, pageable));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<BookingResponse> getBookingWithAllDetails(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingWithAllDetails(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable Long id,
                                                                     @Valid @RequestBody Long passenger_id) {
        return ResponseEntity.ok(bookingService.updateBooking(id,passenger_id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}
