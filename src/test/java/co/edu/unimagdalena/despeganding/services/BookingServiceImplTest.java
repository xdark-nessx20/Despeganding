package co.edu.unimagdalena.despeganding.services;

import co.edu.unimagdalena.despeganding.api.dto.BookingDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.*;
import co.edu.unimagdalena.despeganding.domain.repositories.BookingRepository;
import co.edu.unimagdalena.despeganding.domain.repositories.PassengerRepository;
import co.edu.unimagdalena.despeganding.services.impl.BookingServiceImpl;
import co.edu.unimagdalena.despeganding.services.mappers.BookingMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
    @Mock
    BookingRepository bookingRepository;

    @Mock
    PassengerRepository passengerRepository;

    @InjectMocks
    BookingServiceImpl bookingService;

    @Test
    void shouldCreateBookingAndMapToResponse(){
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(Passenger.builder().id(1L)
                .fullName("Lionel Messi").email("messi.10GOAT@example.com").build()));
        when(bookingRepository.save(any())).thenAnswer(inv -> {
            Booking b = inv.getArgument(0); b.setId(101L); return b;
        });

        var response = bookingService.createBooking(new BookingCreateRequest(1L));

        assertThat(response.id()).isEqualTo(101L);
        assertThat(response.passenger_name()).isEqualTo("Lionel Messi");
        assertThat(response.passenger_email()).isEqualTo("messi.10GOAT@example.com");
    }

    @Test
    void shouldUpdateBookingAndMapToResponse(){
        var passenger = Optional.of(Passenger.builder().id(1L).fullName("Susana Horia Seca")
                .email("susana.horia44@example.com").build());
        var new_passenger = Optional.of(Passenger.builder().id(2L).fullName("Adolfo Jose Hitler Moreno")
                .email("hitlermoreno@example.com").build());
        when(bookingRepository.findById(101L)).thenReturn(Optional.of(Booking.builder().id(101L)
                .createdAt(OffsetDateTime.now().minusDays(1)).passenger(passenger.get()).build()));
        when(passengerRepository.findById(2L)).thenReturn(new_passenger);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        var response = bookingService.updateBooking(101L, 2L);

        assertThat(response.id()).isEqualTo(101L);
        assertThat(response.passenger_name()).isEqualTo("Adolfo Jose Hitler Moreno");
        assertThat(response.passenger_email()).isEqualTo("hitlermoreno@example.com");

    }

    @Test
    void shouldListBookingsBetweenDates(){
        var now = OffsetDateTime.now();
        when(bookingRepository.findByCreatedAtBetween(now.minusDays(1), now)).thenReturn(List.of(
                Booking.builder().id(1L).createdAt(now.minusHours(12)).build(),
                Booking.builder().id(2L).createdAt(now.minusHours(23)).build(),
                Booking.builder().id(3L).createdAt(now.minusMinutes(5)).build()
        ));

        var response = bookingService.listBookingsBetweenDates(now.minusDays(1), now);

        assertThat(response).hasSize(3);
        assertThat(response).allSatisfy(booking -> assertThat(booking.createdAt())
                .isBetween(now.minusDays(1), now.minusMinutes(5)));
    }

    @Test
    void shouldListBookingsByPassengerEmailAndOrderedByRecentlyCreation(){
        var now = OffsetDateTime.now(); var passenger = Optional.of(Passenger.builder().id(1L)
                .fullName("Cristiano Penaldo").email("elcomediante07@example.com").build());
        when(bookingRepository.findByPassenger_EmailIgnoreCaseOrderByCreatedAtDesc(passenger.get().getEmail(),
                Pageable.unpaged())).thenReturn(new PageImpl<>(
                List.of(Booking.builder().id(3L).createdAt(now.minusMinutes(5)).passenger(passenger.get()).build(),
                        Booking.builder().id(1L).createdAt(now.minusHours(12)).passenger(passenger.get()).build(),
                        Booking.builder().id(2L).createdAt(now.minusHours(23)).passenger(passenger.get()).build()
                )
        ));

        var response = bookingService.listBookingsByPassengerEmailAndOrderedMostRecently(passenger.get().getEmail(),
                Pageable.unpaged());

        assertThat(response).hasSize(3);
        assertThat(response).extracting(BookingResponse::passenger_email)
                .allMatch(email -> email.equals("elcomediante07@example.com"));
    }

    @Test
    void shouldGetBookingWithAllDetails(){
        var now = OffsetDateTime.now();
        var passenger = Optional.of(Passenger.builder().id(1L).fullName("Cristiano Penaldo")
                .email("elcomediante07@example.com").build());
        var booking = Optional.of(Booking.builder().id(101L).createdAt(now.minusDays(1))
                .passenger(passenger.get()).build());
        var origin = Optional.of(Airport.builder().id(1001L).code("XD").name("Origin").build());
        var destination = Optional.of(Airport.builder().id(1002L).code("DX").name("Final Destination").build());
        var flight = Flight.builder().id(11L).number("XD0001").origin(origin.get()).destination(destination.get())
                .departureTime(now).arrivalTime(now.plusHours(5)).build();

        BookingMapper.addItem(BookingItem.builder().id(10001L).cabin(Cabin.PREMIUM)
                .price(new BigDecimal("10000000")).segmentOrder(1).flight(flight).build(), booking.get());
        BookingMapper.addItem(BookingItem.builder().id(10002L).cabin(Cabin.BUSINESS)
                .price(new BigDecimal("7500000")).segmentOrder(2).flight(flight).build(), booking.get());
        when(bookingRepository.searchWithAllDetails(101L)).thenReturn(booking);

        var response = bookingService.getBookingWithAllDetails(booking.get().getId());
        var items = response.items();

        assertThat(response.id()).isEqualTo(101L);
        assertThat(response.passenger_name()).isEqualTo("Cristiano Penaldo");
        assertThat(response.passenger_email()).isEqualTo("elcomediante07@example.com");
        assertThat(items).extracting(BookingItemResponse::booking_id).allMatch(id -> id.equals(101L));
        assertThat(items).extracting(BookingItemResponse::flight_id).allMatch(id -> id.equals(11L));
    }
}
