package co.edu.unimagdalena.despeganding.domain.repositories;

import co.edu.unimagdalena.despeganding.domain.entities.Cabin;
import co.edu.unimagdalena.despeganding.domain.entities.SeatInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatInventoryRepository extends JpaRepository<SeatInventory, Long> {
    Optional<SeatInventory> findByFlight_IdAndCabin(Long flight_Id, Cabin cabin);
    List<SeatInventory> findByFlight_Id(Long flight_Id);
    boolean existsByFlight_IdAndCabinAndAvailableSeatsIsGreaterThanEqual(Long flight_Id, Cabin cabin, Integer min);
}
