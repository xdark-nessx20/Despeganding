package co.edu.unimagdalena.despeganding.domain.repositories;

import co.edu.unimagdalena.despeganding.domain.entities.Airline;
import co.edu.unimagdalena.despeganding.domain.entities.Airport;
import co.edu.unimagdalena.despeganding.domain.entities.Flight;
import co.edu.unimagdalena.despeganding.domain.entities.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FlightRepositoryTest extends AbstractRepository{
    @Autowired private FlightRepository flightRepository;
    @Autowired private TestEntityManager testEntityManager;
    @Autowired private AirlineRepository airlineRepository;

    private Flight flight_1, flight_2, flight_3,  flight_4;
    private Airline airline;
    private Airport airport_1, airport_2, airport_3, airport_4;
    private OffsetDateTime staticDate = OffsetDateTime.of(
        2025, 9, 23, 4, 0, 0,0, ZoneOffset.UTC);

    @BeforeEach
    void setUp() {
        airline = Airline.builder().name("despegar.com").code("DC").build();
        airport_1 = Airport.builder().code("BOG").name("El Dorado").city("Bogota").build();
        airport_2 = Airport.builder().code("SMA").name("Simon Bolivar").city("Santa Marta").build();
        airport_3 = Airport.builder().code("XDD").name("miAeropuerto").city("Soledad").build();
        airport_4 = Airport.builder().code("TDF").name("Aero-hard").city("El Dificil").build();

        flight_1 = Flight.builder().airline(airline).origin(airport_1).destination(airport_2).departureTime(staticDate)
            .arrivalTime(staticDate.plusHours(2)).number("0001").build();
        flight_2 = Flight.builder().airline(airline).origin(airport_4).destination(airport_3).departureTime(staticDate.plusHours(2))
            .arrivalTime(staticDate.plusHours(3)).number("0002").build();
        flight_3 = Flight.builder().airline(airline).origin(airport_1).destination(airport_2).departureTime(staticDate.plusDays(1))
            .arrivalTime(staticDate.plusDays(1).plusHours(2)).number("0003").build();
        flight_4 = Flight.builder().airline(airline).origin(airport_2).destination(airport_4).departureTime(staticDate.plusWeeks(1))
            .arrivalTime(staticDate.plusWeeks(1).plusHours(1)).number("0004").build();

        testEntityManager.persistAndFlush(airline);
        testEntityManager.persistAndFlush(airport_1);
        testEntityManager.persistAndFlush(airport_2);
        testEntityManager.persistAndFlush(airport_3);
        testEntityManager.persistAndFlush(airport_4);
        testEntityManager.persistAndFlush(flight_1);
        testEntityManager.persistAndFlush(flight_2);
        testEntityManager.persistAndFlush(flight_3);
        testEntityManager.persistAndFlush(flight_4);
    }

    @Test @DisplayName("Flight: Find flights by airline name")
    void shouldFindByAirline_Name() {
        //Given
        var new_airline = airlineRepository.save(Airline.builder().name("heavy-test").code("HT").build());
        var new_flight = flightRepository.save(Flight.builder().airline(new_airline).number("1001")
            .origin(airport_3).destination(airport_1).departureTime(staticDate.plusDays(4))
            .arrivalTime(staticDate.plusDays(4).plusHours(4)).build());

        //When
        Page<Flight> loaded = flightRepository.findByAirline_Name("despegar.com", Pageable.ofSize(5));

        //Then
        assertThat(loaded.getTotalElements()).isEqualTo(4);
        assertThat(loaded).allSatisfy(flight -> {
            assertThat(flight.getAirline().getName()).isEqualTo("despegar.com");
        });
    }

    @Test @DisplayName("Flight: find flights by origin and destination airport code and departure time between two dates")
    void shouldFindByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween() {
        //When
        Page<Flight> loaded = flightRepository.findByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween("BOG",
            "SMA", staticDate.minusDays(1), staticDate.plusDays(2),  Pageable.ofSize(4));

        //THen
        assertThat(loaded.getTotalElements()).isEqualTo(2);
        assertThat(loaded).allSatisfy(flight -> {
            assertThat(flight.getOrigin().getCode()).isEqualTo("BOG");
            assertThat(flight.getDestination().getCode()).isEqualTo("BOG");
            assertThat(flight.getDepartureTime()).isBetween(staticDate.minusDays(1), staticDate.plusDays(2));
        });
    }

    @Test @DisplayName("Flight: filter flights by departure time between two dates, and optionally, by origin and destination airport")
    void shouldFilterByDepartureTimeBetweenAndOriginAndDestinationOptional() {
        //When
        List<Flight> loaded = flightRepository.filterByOriginAndDestinationOptionalAndDepartureTimeBetween(airport_1, null,
            staticDate.plusHours(1),  staticDate.plusWeeks(1).plusHours(5));

        //Then
        assertThat(loaded).hasSize(2);
        assertThat(loaded).allSatisfy(flight -> {
            assertThat(flight.getDepartureTime()).isBetween(staticDate.minusDays(1), staticDate.plusDays(2));
        });
    }

    @Test @DisplayName("Flight: find flights which has the same tags from a list of tags")
    void shouldFindByHavingSomeTags() {
        //Given
        Tag tag1 = Tag.builder().name("tag1").build();
        Tag tag2 = Tag.builder().name("pruebi-tag").build();
        Tag tag3 = Tag.builder().name("tag-dado").build();

        var new_flight = flightRepository.save(Flight.builder().airline(airline).number("1001")
                .origin(airport_3).destination(airport_1).departureTime(staticDate.plusDays(4))
                .arrivalTime(staticDate.plusDays(4).plusHours(4)).build());

        flight_1.addTag(tag1);
        flight_1.addTag(tag2);

        flight_2.addTag(tag3);
        flight_2.addTag(tag1);

        flight_3.addTag(tag2);
        flight_3.addTag(tag3);

        flight_4.addTag(tag1);
        flight_4.addTag(tag2);

        new_flight.addTag(tag1);
        new_flight.addTag(tag2);

        List<String> tags = List.of("tag1", "pruebi-tag");

        //When
        List<Flight> loaded = flightRepository.findFlightsWithTheseTags(tags, (long) tags.size());

        //Then
        assertThat(loaded).hasSize(3);
        assertThat(loaded).allSatisfy(flight -> {
            flight.getTags().forEach(tag -> {
                assertThat(tags.contains(tag.getName())).isTrue();
            });
        });
    }
}
