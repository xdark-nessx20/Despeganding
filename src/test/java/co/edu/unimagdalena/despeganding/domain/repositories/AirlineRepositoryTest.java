package co.edu.unimagdalena.despeganding.domain.repositories;

import co.edu.unimagdalena.despeganding.domain.entities.Airline;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AirlineRepositoryTest extends AbstractRepository{
    @Autowired AirlineRepository airlineRepository;

    @Test @DisplayName("Airline: find by code")
    void shouldFindByCode() {
        //Given
        airlineRepository.save(Airline.builder().code("DC").name("despegar.com").build());
        airlineRepository.save(Airline.builder().code("BG").name("nosexd").build());

        //When
        Optional<Airline> airline = airlineRepository.findByCode("DC");
        //Then
        assertThat(airline).isPresent();


    }
}
