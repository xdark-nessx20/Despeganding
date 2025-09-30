package co.edu.unimagdalena.despeganding.services;

import co.edu.unimagdalena.despeganding.api.dto.BookingDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.Booking;
import co.edu.unimagdalena.despeganding.domain.entities.BookingItem;
import co.edu.unimagdalena.despeganding.domain.entities.Cabin;
import co.edu.unimagdalena.despeganding.domain.entities.Flight;
import co.edu.unimagdalena.despeganding.domain.repositories.BookingItemRepository;
import co.edu.unimagdalena.despeganding.domain.repositories.BookingRepository;
import co.edu.unimagdalena.despeganding.domain.repositories.FlightRepository;
import co.edu.unimagdalena.despeganding.services.impl.BookingItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingItemServiceImplTest {
    @Mock BookingItemRepository bookingItemRepository;
    @Mock FlightRepository flightRepository;
    @Mock BookingRepository bookingRepository;

    @InjectMocks BookingItemServiceImpl bookingItemServiceImpl;

    @Test
    void shouldAddBookingItemAndMapToResponse(){
        var now = OffsetDateTime.now();
        when(flightRepository.findById(1L)).thenReturn(Optional.of(Flight.builder().id(1L).number("XD0001").departureTime(now)
                .arrivalTime(now.plusHours(5)).build()));
        when(bookingRepository.findById(101L)).thenReturn(Optional.of(Booking.builder().id(101L).createdAt(now).build()));

        var response = bookingItemServiceImpl.addBookingItem(101L, 1L,
                new BookingItemCreateRequest("ECONOMY", new BigDecimal(1000000), 1));

        assertThat(response).isNotNull();
        assertThat(response.flight_id()).isEqualTo(1L);
        assertThat(response.booking_id()).isEqualTo(101L);
    }

    @Test
    void shouldUpdateBookingItemAndMapToResponse(){

    }

    @Test
    void shouldListBookingItemsByBooking(){
        var booking = Optional.of(Booking.builder().id(101L).createdAt(OffsetDateTime.now()).build());
        var booking_2 = Optional.of(Booking.builder().id(102L).createdAt(OffsetDateTime.now()).build());
        when(bookingRepository.findById(101L)).thenReturn(booking);
        when(bookingItemRepository.findByBooking_IdOrderBySegmentOrder(101L)).thenReturn(List.of(
                BookingItem.builder().id(1011L).cabin(Cabin.ECONOMY).booking(booking.get()).price(new BigDecimal(1000000)).segmentOrder(1).build(),
                BookingItem.builder().id(1012L).cabin(Cabin.PREMIUM).booking(booking_2.get()).price(new BigDecimal(7000000)).segmentOrder(1).build(),
                BookingItem.builder().id(1013L).cabin(Cabin.PREMIUM).booking(booking.get()).price(new BigDecimal(4000000)).segmentOrder(2).build(),
                BookingItem.builder().id(1014L).cabin(Cabin.PREMIUM).booking(booking_2.get()).price(new BigDecimal(4000000)).segmentOrder(2).build(),
                BookingItem.builder().id(1015L).cabin(Cabin.BUSINESS).booking(booking.get()).price(new BigDecimal(2000000)).segmentOrder(3).build()
        ));

        var response = bookingItemServiceImpl.listBookingItemsByBooking(101L);

        assertThat(response).hasSize(3);
        assertThat(response).extracting(BookingItemResponse::booking_id)
                .allMatch(id -> id.equals(101L));
    }

    //IDK if we've to test all service methods... 'cause I think so, but, the professor wants to?
}
