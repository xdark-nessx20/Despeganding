package co.edu.unimagdalena.despeganding.services.impl;

import co.edu.unimagdalena.despeganding.domain.entities.Booking;
import co.edu.unimagdalena.despeganding.domain.repositories.BookingRepository;
import co.edu.unimagdalena.despeganding.domain.repositories.PassengerRepository;
import co.edu.unimagdalena.despeganding.exceptions.NotFoundException;
import co.edu.unimagdalena.despeganding.services.BookingService;
import co.edu.unimagdalena.despeganding.api.dto.BookingDTOs.*;
import co.edu.unimagdalena.despeganding.services.mappers.BookingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service @RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;

    @Override @Transactional
    public BookingResponse createBooking(BookingCreateRequest request) {
        var passenger = passengerRepository.findById(request.passenger_id()).orElseThrow(
                () -> new NotFoundException("Passenger %d not found.".formatted(request.passenger_id()))
        );
        var booking = Booking.builder().createdAt(OffsetDateTime.now()).passenger(passenger).build();
        return BookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingResponse getBooking(Long id) {
        return bookingRepository.findById(id).map(BookingMapper::toResponse).orElseThrow(
                () -> new NotFoundException("Booking %d not found.".formatted(id))
        );
    }

    @Override
    public List<BookingResponse> listBookingsBetweenDates(OffsetDateTime start, OffsetDateTime end) {
        if (start.isAfter(end)) throw new IllegalArgumentException("Start date is after end date.");
        return bookingRepository.findByCreatedAtBetween(start, end).stream()
                .map(BookingMapper::toResponse).toList();
    }

    @Override
    public Page<BookingResponse> listBookingsByPassengerEmailAndOrderedMostRecently(String passenger_email, Pageable pageable) {
        return bookingRepository.findByPassenger_EmailIgnoreCaseOrderByCreatedAtDesc(passenger_email,
                pageable).map(BookingMapper::toResponse);
    }

    @Override
    public BookingResponse getBookingWithAllDetails(Long id) {
        return bookingRepository.searchWithAllDetails(id).map(BookingMapper::toResponse).orElseThrow(
                () -> new NotFoundException("Booking %d not found.".formatted(id))
        );
    }

    @Override @Transactional
    public BookingResponse updateBooking(Long id, Long passenger_id) {
        var booking = bookingRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Booking %d not found.".formatted(id))
        );
        if (passenger_id != null) {
            var new_passenger = passengerRepository.findById(passenger_id).orElseThrow(
                    () -> new NotFoundException("Passenger %d not found.".formatted(id))
            );
            booking.setPassenger(new_passenger);
        }
        return BookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override @Transactional
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
