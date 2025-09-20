package co.edu.unimagdalena.despeganding.domine.repositories;

import co.edu.unimagdalena.despeganding.domine.entities.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
