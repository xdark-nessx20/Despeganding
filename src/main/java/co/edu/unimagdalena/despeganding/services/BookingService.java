package co.edu.unimagdalena.despeganding.services;

import co.edu.unimagdalena.despeganding.api.dto.BookingDTOs.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingCreateRequest request);
    BookingResponse getBooking(Long id);
    List<BookingResponse> listBookingsBetweenDates(OffsetDateTime start, OffsetDateTime end);
    Page<BookingResponse> listBookingsByPassengerEmailAndOrderedMostRecently(String passenger_email,
                                                                             Pageable pageable);
    BookingResponse getBookingWithAllDetails(Long id);
    BookingResponse updateBooking(Long id, Long passenger_id);
    void deleteBooking(Long id);
}
