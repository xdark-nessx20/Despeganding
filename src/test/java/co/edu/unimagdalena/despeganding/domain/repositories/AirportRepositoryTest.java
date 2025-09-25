package co.edu.unimagdalena.despeganding.domain.repositories;

import co.edu.unimagdalena.despeganding.domain.entities.Airport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AirportRepositoryTest extends AbstractRepository{
    @Autowired private AirportRepository airportRepository;
    @Autowired private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        //Given
        Airport airport_1 = Airport.builder().code("BOG").city("Bogota").name("El Dorado").build(),
                airport_2 = Airport.builder().code("BGD").city("Bogota").name("Other Airport").build(),
                airport_3 = Airport.builder().code("MED").city("Medellin").name("A medallo airport").build(),
                airport_4 = Airport.builder().code("WIP").city("Bogota").name("Wipiii").build();
        testEntityManager.persistAndFlush(airport_1);
        testEntityManager.persistAndFlush(airport_2);
        testEntityManager.persistAndFlush(airport_3);
        testEntityManager.persistAndFlush(airport_4);
    }

    @Test @DisplayName("Airport: find by code")
    void shouldFindByCode() {
        // When
        Optional<Airport> loaded = airportRepository.findByCode("MED");
        //Then
        assertThat(loaded).isPresent();
        assertThat(loaded.get().getCode()).isEqualTo("MED");
    }

    @Test @DisplayName("Airport: find by city")
    void findByCodeAndCity() {
        // When
        List<Airport> loaded = airportRepository.findByCity("Bogota");

        //Then
        assertThat(loaded).isNotEmpty();
        assertThat(loaded).hasSize(3);
    }
}
