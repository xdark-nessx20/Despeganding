package co.edu.unimagdalena.despeganding.domain.repositories;

import co.edu.unimagdalena.despeganding.domain.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SeatInventoryRepositoryTest extends AbstractRepository {
    @Autowired
    SeatInventoryRepository seatInventoryRepository;

    @Autowired
    TestEntityManager entityManager;

    private Flight flight1, flight2;

    @BeforeEach
    void setUp() {
        setupTestData();
    }

    private void setupTestData() {
        Airline airline = Airline.builder().code("LV").name("Los Viajes de Ella").build();
        entityManager.persistAndFlush(airline);

        Airport origin = Airport.builder().code("EP").name("El Plateado").city("Naguaraibo").build();
        entityManager.persistAndFlush(origin);

        Airport destination = Airport.builder().code("AL").name("Alert Airport").city("Arepa City").build();
        entityManager.persistAndFlush(destination);

        flight1 = Flight.builder().number("AA001").departureTime(OffsetDateTime.now().plusDays(1)).arrivalTime(OffsetDateTime.now().plusDays(1)
                .plusHours(2)).airline(airline).origin(origin).destination(destination).build();
        entityManager.persistAndFlush(flight1);

        entityManager.clear();
    }

    @Test
    @DisplayName("Seat Inventory: Find by Flight ID and cabin")
    void shouldFindByFlight_IdAndCabin() {
        //Given
        seatInventoryRepository.save(SeatInventory.builder().cabin(Cabin.ECONOMY).totalSeats(180).availableSeats(120).flight(flight1).build());
        seatInventoryRepository.save(SeatInventory.builder().cabin(Cabin.BUSINESS).totalSeats(20).availableSeats(15).flight(flight1).build());

        //When - Then
        Optional<SeatInventory> seatInventory = seatInventoryRepository.findByFlight_IdAndCabin(flight1.getId(), Cabin.ECONOMY);

        assertThat(seatInventory)
                .isPresent()
                .get()
                .satisfies(inventory -> {
                    assertThat(inventory.getCabin()).isEqualTo(Cabin.ECONOMY);
                    assertThat(inventory.getTotalSeats()).isEqualTo(180);
                    assertThat(inventory.getAvailableSeats()).isEqualTo(120);
                });

        // Verificar que NO encuentra algo que no existe
        assertThat(seatInventoryRepository.findByFlight_IdAndCabin(flight1.getId(), Cabin.PREMIUM)).isEmpty();
    }

    @Test
    @DisplayName("Seat Inventory: Return TRUE if (by flight id, cabin) the available seats availableSeats >= min")
    void shouldVerifyIfExistsByFlight_IdAndCabinAndAvailableSeatsIsGreaterThanEqual() {
        // Given
        seatInventoryRepository.save(SeatInventory.builder().cabin(Cabin.ECONOMY).totalSeats(180).availableSeats(120).flight(flight1).build());

        seatInventoryRepository.save(SeatInventory.builder().cabin(Cabin.BUSINESS).totalSeats(20).availableSeats(5).flight(flight1).build());

        // When - Then
        assertThat(seatInventoryRepository.existsByFlight_IdAndCabinAndAvailableSeatsIsGreaterThanEqual(flight1.getId(), Cabin.ECONOMY, 100)).isTrue();

        assertThat(seatInventoryRepository.existsByFlight_IdAndCabinAndAvailableSeatsIsGreaterThanEqual(flight1.getId(), Cabin.ECONOMY, 200)).isFalse();

        assertThat(seatInventoryRepository.existsByFlight_IdAndCabinAndAvailableSeatsIsGreaterThanEqual(flight1.getId(), Cabin.BUSINESS, 1)).isTrue();

        assertThat(seatInventoryRepository.existsByFlight_IdAndCabinAndAvailableSeatsIsGreaterThanEqual(flight1.getId(), Cabin.PREMIUM, 1)).isFalse();
    }
}
