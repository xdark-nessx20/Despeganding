package co.edu.unimagdalena.despeganding.domain.repositories;

import co.edu.unimagdalena.despeganding.domain.entities.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class BookingRepositoryTest extends AbstractRepository{
    @Autowired BookingRepository bookingRepository;
    @Autowired PassengerRepository passengerRepository;
    @Autowired FlightRepository flightRepository;
    @Autowired AirportRepository airportRepository;
    @Autowired AirlineRepository airlineRepository;
    @Autowired BookingItemRepository bookingItemRepository;

    @Test @DisplayName("Booking: find by createdAt and updatedAt between")
    void shouldFindByCreatedAtBetweenAndUpdatedAtBetween() {
        //Given
        var passenger = passengerRepository.save(Passenger.builder()
            .fullName("Andres Rudas")
            .email("rudas@example.com").build());

        var airline = airlineRepository.save(Airline.builder()
            .code("XD").name("despegar.com").build());

        var origin_airport = airportRepository.save(Airport.builder()
            .code("SMA").name("despeganding").city("Santa Marta").build());

        var destination_airport = airportRepository.save(Airport.builder()
            .code("BOG").name("EL DORADO").city("Bogota").build());

        var now = OffsetDateTime.now(ZoneOffset.UTC);

        var booking = bookingRepository.save(Booking.builder()
            .createdAt(now.minusDays(3)).updatedAt(now).passenger(passenger).build());

        var flight = flightRepository.save(Flight.builder()
            .airline(airline).origin(origin_airport).destination(destination_airport)
            .departureTime(now.plusHours(1)).arrivalTime(now.plusHours(3)).build());

        bookingItemRepository.save(BookingItem.builder()
            .cabin(Cabin.ECONOMY).price(new BigDecimal("478000.00")).segmentOrder(5)
            .booking(booking).flight(flight).build());

        //When
        var loaded = bookingRepository.findByCreatedAtBetween(now.minusDays(4), now.minusDays(1));
        var loaded_2 = bookingRepository.findByCreatedAtBetween(now.minusDays(2), now.minusDays(1));

        //Then
        assertThat(loaded.getFirst().getCreatedAt())
            .isBetween(now.minusDays(4), now.minusDays(1));
        assertThat(loaded_2.getFirst().getUpdatedAt())
            .isBetween(now.minusDays(2), now.minusDays(1));
    }

    @Test @DisplayName("Booking: find bookings by passenger email ignore case and ordered by created at desc")
    void shouldFindByPassengerEmailIgnoreCaseAndOrderedByCreatedAtDesc(){
        //Given
        var passenger = passengerRepository.save(Passenger.builder()
                .fullName("Nestor Saumeth")
                .email("nsaumeth@example.com").build());

        var airline = airlineRepository.save(Airline.builder()
                .code("XD").name("despegar.com").build());

        var origin_airport = airportRepository.save(Airport.builder()
                .code("SMA").name("despeganding").city("Santa Marta").build());

        var destination_airport = airportRepository.save(Airport.builder()
                .code("BOG").name("EL DORADO").city("Bogota").build());

        var now = OffsetDateTime.now(ZoneOffset.UTC);

        var booking = bookingRepository.save(Booking.builder()
                .createdAt(now.minusDays(3)).updatedAt(now).passenger(passenger).build());
        var booking_2 = bookingRepository.save(Booking.builder()
                .createdAt(now.minusDays(2)).updatedAt(now).passenger(passenger).build());

        var flight = flightRepository.save(Flight.builder()
                .airline(airline).origin(origin_airport).destination(destination_airport)
                .departureTime(now.plusHours(1)).arrivalTime(now.plusHours(3)).build());

        bookingItemRepository.saveAll(List.of(
            BookingItem.builder()
                    .cabin(Cabin.BUSINESS).price(new BigDecimal("1478000.00")).segmentOrder(5)
                    .booking(booking).flight(flight).build(),
            BookingItem.builder()
                    .cabin(Cabin.BUSINESS).price(new BigDecimal("1478000.00")).segmentOrder(5)
                    .booking(booking_2).flight(flight).build()
            )
        );

        //When
        var loaded = bookingRepository.findByPassenger_EmailIgnoreCaseOrderByCreatedAtDesc(
            "nSauMEtH@exAmPle.cOm", Pageable.ofSize(5));

        //Then
        assertThat(loaded.getContent().getFirst().getPassenger().getEmail())
            .isEqualTo("nsaumeth@example.com");
    }
}
