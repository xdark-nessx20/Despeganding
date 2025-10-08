package co.edu.unimagdalena.despeganding.api;

import co.edu.unimagdalena.despeganding.api.dto.BookingDTOs.*;
import co.edu.unimagdalena.despeganding.services.BookingItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

//Validated sets up the vars validating in the controller
@RestController @RequiredArgsConstructor @Validated
@RequestMapping("/api/v1/bookings/{bookingId}/items")
public class BookingItemController {
    private final BookingItemService bookingItemService;

    @PostMapping //A PathVariable var gotta get name of the variable in the MappingURL
    public ResponseEntity<BookingItemResponse> add(@PathVariable Long bookingId, @Valid @RequestBody
                                                   BookingItemCreateRequest request, UriComponentsBuilder uriBuilder) {
        var body = bookingItemService.addBookingItem(bookingId, request);
        //The new URL for the new resource
        var location = uriBuilder.path("/api/v1/bookings/{bookingId}/items/{itemId}")
                .buildAndExpand(bookingId, body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<BookingItemResponse> get(@PathVariable Long bookingId, @PathVariable Long itemId) {
        return ResponseEntity.ok(bookingItemService.getBookingItem(itemId));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<BookingItemResponse> patch(@PathVariable Long bookingId, @PathVariable Long itemId,
                                                     @Valid @RequestBody BookingItemUpdateRequest request) {
        return ResponseEntity.ok(bookingItemService.updateBookingItem(itemId, request));
    }

    @GetMapping
    public ResponseEntity<List<BookingItemResponse>> listByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingItemService.listBookingItemsByBooking(bookingId));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> delete(@PathVariable Long bookingId, @PathVariable Long itemId) {
        bookingItemService.deleteBookingItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
