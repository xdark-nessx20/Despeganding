package co.edu.unimagdalena.despeganding.domain.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

public class FlightRepositoryTest extends AbstractRepository{
    @Autowired FlightRepository flightRepository;
    @Autowired private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {

    }

    @Test @DisplayName("Flight: Find flights by airline name")
    void shouldFindByAirline_Name() {
    }

    @Test @DisplayName("Flight: find flights by origin and destination airport code and departure time between two dates")
    void shouldFindByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween() {
    }

    @Test @DisplayName("Flight: filter flights by departure time between two dates, and optionally, by origin and destination airport")
    void shouldFilterByDepartureTimeBetweenAndOriginAndDestinationOptional() {
    }

    @Test @DisplayName("Flight: find flights which has the same tags from a list of tags")
    void shouldFindByHavingSomeTags() {

    }
}
