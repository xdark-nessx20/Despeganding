package co.edu.unimagdalena.despeganding.domine.repositories;

import co.edu.unimagdalena.despeganding.domine.entities.Cabin;
import co.edu.unimagdalena.despeganding.domine.entities.SeatInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatInventoryRepository extends JpaRepository<SeatInventory, Long> {
    Optional<SeatInventory> findByFlightIdAndCabin(Long flightId, Cabin cabin);

    boolean existsByFlightIdAndCabinAndAvailableSeatsIsGreaterThanEqual(Long flightId, Cabin cabin, Integer min);
}
