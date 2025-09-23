package co.edu.unimagdalena.despeganding.domain.repositories;

import co.edu.unimagdalena.despeganding.domain.entities.PassengerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassengerProfileRepository extends JpaRepository<PassengerProfile, Long> {
    List<PassengerProfile> findByCountryCode(String countryCode);
}
