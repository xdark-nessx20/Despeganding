package co.edu.unimagdalena.despeganding.services.impl;

import co.edu.unimagdalena.despeganding.api.dto.BookingDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.BookingItem;
import co.edu.unimagdalena.despeganding.domain.entities.Cabin;
import co.edu.unimagdalena.despeganding.domain.repositories.BookingItemRepository;
import co.edu.unimagdalena.despeganding.domain.repositories.BookingRepository;
import co.edu.unimagdalena.despeganding.domain.repositories.FlightRepository;
import co.edu.unimagdalena.despeganding.exceptions.NotFoundException;
import co.edu.unimagdalena.despeganding.services.BookingItemService;
import co.edu.unimagdalena.despeganding.services.mappers.BookingMapper;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service @RequiredArgsConstructor
public class BookingItemServiceImpl implements BookingItemService {
    private final BookingItemRepository bookingItemRepository;
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;

    @Override @Transactional
    public BookingItemResponse addBookingItem(@Nonnull Long booking_id, @Nonnull Long flight_id, BookingItemCreateRequest request) {
        var flight =  flightRepository.findById(flight_id).orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(flight_id)));
        var booking = bookingRepository.findById(booking_id).orElseThrow(
                () -> new NotFoundException("Booking %d not found".formatted(booking_id))
        );

        var bookingItem = BookingItem.builder().cabin(Cabin.valueOf(request.cabin())).price(request.price()).segmentOrder(request.segmentOrder())
                .flight(flight).build();
        BookingMapper.addItem(bookingItem, booking);

        return BookingMapper.toItemResponse(bookingItem);
    }

    @Override
    public BookingItemResponse getBookingItem(@Nonnull Long id) {
        return bookingItemRepository.findById(id).map(BookingMapper::toItemResponse).orElseThrow(
                () -> new NotFoundException("Booking Item %d not found".formatted(id))
        );
    }

    @Override @Transactional
    public BookingItemResponse updateBookingItem(@Nonnull Long id, BookingItemUpdateRequest request, @Nonnull Long flight_id) {
        var bookingItem = bookingItemRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Booking Item %d not found".formatted(id))
        );
        BookingMapper.itemPatch(bookingItem, request);

        var flight = flightRepository.findById(flight_id).orElseThrow(
                () -> new NotFoundException("Flight %d not found".formatted(flight_id))
        );
        bookingItem.setFlight(flight);

        return BookingMapper.toItemResponse(bookingItem);
    }

    @Override @Transactional
    public void deleteBookingItem(@Nonnull Long id) {
        bookingItemRepository.deleteById(id);
    }

    @Override
    public List<BookingItemResponse> listBookingItemsByBooking(@Nonnull Long booking_id) {
        var booking = bookingRepository.findById(booking_id).orElseThrow(
                () -> new NotFoundException("Booking %d not found".formatted(booking_id))
        );
        return bookingItemRepository.findByBooking_IdOrderBySegmentOrder(booking.getId()).stream().map(BookingMapper::toItemResponse).toList();
    }

    @Override
    public Long countReservedSeatsByFlightAndCabin(@Nonnull Long flight_id, String cabin) {
        var flight = flightRepository.findById(flight_id).orElseThrow(
                () -> new NotFoundException("Flight %d not found".formatted(flight_id))
        );
        return bookingItemRepository.countReservedSeatsByFlight_IdAndCabin(flight.getId(), Cabin.valueOf(cabin));
    }

    @Override
    public BigDecimal calculateTotalPriceByBooking(@Nonnull Long booking_id) {
        var booking = bookingRepository.findById(booking_id).orElseThrow(
                () -> new NotFoundException("Booking %d not found".formatted(booking_id))
        );
        return bookingItemRepository.calculateTotalByBooking_Id(booking.getId());
    }
}
