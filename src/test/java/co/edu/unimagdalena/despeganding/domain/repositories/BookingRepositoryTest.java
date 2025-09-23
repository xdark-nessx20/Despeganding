package co.edu.unimagdalena.despeganding.domain.repositories;

import co.edu.unimagdalena.despeganding.domain.entities.Passenger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class BookingRepositoryTest extends AbstractRepository {
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    PassengerRepository passengerRepository;

    @Test
    @DisplayName("Booking: find by createdAt and updatedAt between")
    void findByCreatedAtBetweenAndUpdatedAtBetween() {
        //Given
        var passenger = passengerRepository.save(Passenger.builder()
                .fullName("Andres Rudas")
                .email("rudas@gmail.com").build());
    }
}
