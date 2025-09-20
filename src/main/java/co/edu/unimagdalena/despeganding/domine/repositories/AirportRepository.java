package co.edu.unimagdalena.despeganding.domine.repositories;

import co.edu.unimagdalena.despeganding.domine.entities.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, Long> {
}
