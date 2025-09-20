package co.edu.unimagdalena.despeganding.domine.repositories;

import co.edu.unimagdalena.despeganding.domine.entities.PassengerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerProfileRepository extends JpaRepository<PassengerProfile, Long> {
}
