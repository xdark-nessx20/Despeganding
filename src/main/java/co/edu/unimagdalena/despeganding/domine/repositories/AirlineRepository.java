package co.edu.unimagdalena.despeganding.domine.repositories;

import co.edu.unimagdalena.despeganding.domine.entities.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirlineRepository extends JpaRepository<Airline, Long> {
}
