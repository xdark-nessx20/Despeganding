package co.edu.unimagdalena.despeganding.domain.repositories;

import co.edu.unimagdalena.despeganding.domain.entities.Airport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class AirportRepositoryTest extends AbstractRepository{
    @Autowired AirportRepository airportRepository;

    @Test @DisplayName("Airport: find by code and by city")
    void findByCodeAndCity() {
        //Given
        airportRepository.save(Airport.builder().code("BOG").city("Bogota").name("El Dorado").build());
        airportRepository.save(Airport.builder().code("BGD").city("Bogota").name("Other Airport").build());
        airportRepository.save(Airport.builder().code("MED").city("Medellin").name("A medallo airport").build());
        airportRepository.save(Airport.builder().code("WIP").city("Bogota").name("Wipiii").build());

        // When/Then
        assertThat(airportRepository.findByCode("MED")).isPresent();
        assertThat(airportRepository.findByCity("Bogota")).isNotEmpty();

    }
}
