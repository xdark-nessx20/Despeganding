package co.edu.unimagdalena.despeganding.domain.repositories;

import co.edu.unimagdalena.despeganding.domain.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookingItemRepositoryTest extends AbstractRepository {

    @Autowired
    BookingItemRepository bookingItemRepo;

    @Autowired
    TestEntityManager entityManager;

    private Booking booking1;
    private Flight flight1;
    private Flight flight2;

    @BeforeEach
    void setUp() {
        setupTestData();
    }

    //Wipi, segun varias fuentes el SetupTestData es pa cargar las entidades correspondientes y que no tire errores (mejor creer)
    //Entity Manager: Sirve para agilizar las cosas, PersistAndFlush hace que se cargue en memoria y en la DB al instante (futuro)
    private void setupTestData() {
        Passenger passenger = Passenger.builder().fullName("Elma Estro").email("elmaspapiao@testmethis.com").build();
        entityManager.persistAndFlush(passenger);

        Airline airline = Airline.builder().name("American Airlines").code("AA").build();
        entityManager.persistAndFlush(airline);

        Airport origin = Airport.builder().name("Origin Airport").code("OR").city("Origin City").build();
        entityManager.persistAndFlush(origin);

        Airport destination = Airport.builder().name("Destination Airport").code("DS").city("Dest City").build();
        entityManager.persistAndFlush(destination);

        // Nojoda cule poco e vainas
        flight1 = Flight.builder().number("AA001").departureTime(OffsetDateTime.now().plusDays(1)).arrivalTime(OffsetDateTime.now().plusDays(1)
                .plusHours(2)).airline(airline).origin(origin).destination(destination).build();
        entityManager.persistAndFlush(flight1);

        flight2 = Flight.builder().number("AA002").departureTime(OffsetDateTime.now().plusDays(2)).arrivalTime(OffsetDateTime.now().plusDays(2).
                plusHours(3)).airline(airline).origin(origin).destination(destination).build();
        entityManager.persistAndFlush(flight2);


        booking1 = Booking.builder().createdAt(OffsetDateTime.now()).passenger(passenger).build();
        entityManager.persistAndFlush(booking1);

        entityManager.clear();
    }

    //.............................................................................................................................................................

    @Test
    @DisplayName("BookingItem: find by booking ID ordered by segment order")
    void shouldFindByBooking_IdOrderBySegmentOrder() {
        // Given
        bookingItemRepo.save(BookingItem.builder().cabin(Cabin.BUSINESS).price(new BigDecimal("300.00")).segmentOrder(2).booking(booking1).
                flight(flight2).build());

        bookingItemRepo.save(BookingItem.builder().cabin(Cabin.ECONOMY).price(new BigDecimal("150.00")).segmentOrder(1).booking(booking1)
                .flight(flight1).build());

        // When / Then
        List<BookingItem> items = bookingItemRepo.findByBooking_IdOrderBySegmentOrder(booking1.getId());

        assertThat(items).hasSize(2)
                .extracting(BookingItem::getSegmentOrder)
                .containsExactly(1, 2);

        assertThat(items.getFirst())
                .satisfies(item -> {
                    assertThat(item.getCabin()).isEqualTo(Cabin.ECONOMY);
                    assertThat(item.getPrice()).isEqualByComparingTo(new BigDecimal("150.00"));
                });
    }

//.............................................................................................................................................................

    @Test
    @DisplayName("BookingItem: calculates total by booking ID")
    void shouldCalculateTotalByBooking_Id() {
        // Given
        bookingItemRepo.save(BookingItem.builder().cabin(Cabin.ECONOMY).price(new BigDecimal("150.00")).segmentOrder(1).booking(booking1)
                .flight(flight1).build());

        bookingItemRepo.save(BookingItem.builder()
                .cabin(Cabin.BUSINESS).price(new BigDecimal("300.00")).segmentOrder(2).booking(booking1).flight(flight2).build());

        // When / Then
        BigDecimal total = bookingItemRepo.calculateTotalByBooking_Id(booking1.getId());

        assertThat(total).isEqualByComparingTo(new BigDecimal("450.00"));
    }

    @Test
    @DisplayName("BookingItem: count reserved seats by flight and cabin")
    void shouldCountReservedSeatsByFlightAndCabin() {
        // Given
        bookingItemRepo.save(BookingItem.builder().cabin(Cabin.ECONOMY).price(new BigDecimal("150.00")).segmentOrder(1).booking(booking1)
                .flight(flight1).build());

        bookingItemRepo.save(BookingItem.builder().cabin(Cabin.ECONOMY).price(new BigDecimal("175.00")).segmentOrder(1).booking(booking1)
                .flight(flight1).build());

        bookingItemRepo.save(BookingItem.builder().cabin(Cabin.BUSINESS).price(new BigDecimal("400.00")).segmentOrder(1).booking(booking1)
                .flight(flight1).build());

        // When / Then
        Long economyCount = bookingItemRepo.countReservedSeatsByFlightAndCabin(flight1.getId(), Cabin.ECONOMY);
        Long businessCount = bookingItemRepo.countReservedSeatsByFlightAndCabin(flight1.getId(), Cabin.BUSINESS);
        Long premiumCount = bookingItemRepo.countReservedSeatsByFlightAndCabin(flight1.getId(), Cabin.PREMIUM);

        assertThat(economyCount).isEqualTo(2);
        assertThat(businessCount).isEqualTo(1);
        assertThat(premiumCount).isEqualTo(0);
    }
}
