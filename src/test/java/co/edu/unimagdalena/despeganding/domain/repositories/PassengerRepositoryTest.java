package co.edu.unimagdalena.despeganding.domain.repositories;

import co.edu.unimagdalena.despeganding.domain.entities.Passenger;
import co.edu.unimagdalena.despeganding.domain.entities.PassengerProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class PassengerRepositoryTest extends AbstractRepository {
    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    PassengerProfileRepository passengerProfileRepository;

    @Test
    @DisplayName("Passenger: find by email (ignore case) and fetch the profile")
    void shouldFindByEmailIgnoreCaseAndFetchProfileByEmail() {
        // Given
        var profile = PassengerProfile.builder().phone("300 000 0001").countryCode("+57").build();
        var passenger = Passenger.builder().fullName("Zacarias Flores Del Campo").email("zfdc24@DEMO.COM").profile(profile).build();
        passengerProfileRepository.save(profile);
        passengerRepository.save(passenger);

        // When
        Optional<Passenger> byEmail = passengerRepository.findByEmailIgnoreCase("ZFDC24@DEMO.COM");
        Optional<Passenger> withProfile = passengerRepository.findByEmailIgnoreCaseWithProfile("zfdc24@DEMO.COM");

        // Then
        assertThat(byEmail).isPresent();
        assertThat(byEmail.get().getEmail()).isEqualTo("zfdc24@DEMO.COM");
        assertThat(withProfile).isPresent();
        assertThat(withProfile.get().getProfile().getCountryCode()).isEqualTo("+57");
    }
}
