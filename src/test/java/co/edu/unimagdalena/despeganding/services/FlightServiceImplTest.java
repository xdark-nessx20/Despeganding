package co.edu.unimagdalena.despeganding.services;

import co.edu.unimagdalena.despeganding.api.dto.FlightDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.Airline;
import co.edu.unimagdalena.despeganding.domain.entities.Airport;
import co.edu.unimagdalena.despeganding.domain.entities.Flight;
import co.edu.unimagdalena.despeganding.domain.repositories.AirlineRepository;
import co.edu.unimagdalena.despeganding.domain.repositories.AirportRepository;
import co.edu.unimagdalena.despeganding.domain.repositories.FlightRepository;
import co.edu.unimagdalena.despeganding.domain.repositories.TagRepository;
import co.edu.unimagdalena.despeganding.services.impl.FlightServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;

import java.time.OffsetDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FlightServiceImplTest {
    @Mock FlightRepository flightRepository;
    @Mock AirportRepository airportRepository;
    @Mock AirlineRepository airlineRepository;
    @Mock TagRepository tagRepository;

    @InjectMocks FlightServiceImpl flightService;

    @Test
    void shouldCreateFlightAndMapToResponse() {
        when(airlineRepository.findById(1L)).thenReturn(Optional.of(Airline.builder().id(1L).code("XD").name("Airline With Down").build()));
        when(airportRepository.findById(1L)).thenReturn(Optional.of(Airport.builder().id(1L).code("ZZZ").name("Fake Madrid").city("Madrid").build()));
        when(airportRepository.findById(2L)).thenReturn(Optional.of(Airport.builder().id(2L).code("OMG").name("Barca-port").city("Barcelona").build()));
        when(flightRepository.save(any())).thenAnswer(invocation -> {
            Flight flight =  invocation.getArgument(0);
            flight.setId(10L);
            return flight;
        });

        var now = OffsetDateTime.now();
        var response = flightService.createFlight(new FlightCreateRequest("XD0001", now, now.plusHours(2)), 1L, 1L, 2L);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.number()).isEqualTo("XD0001");
        assertThat(response.airline_id()).isEqualTo(1L);
        assertThat(response.origin_airport_id()).isEqualTo(1L);
        assertThat(response.destination_airport_id()).isEqualTo(2L);
    }

    @Test
    void shouldUpdateFlightAndMapToResponse() {

    }

    @Test
    void shouldAddTagToFlightAndMapToResponse() {
    }

    @Test
    void shouldRemoveTagFromFlightAndMapToResponse() {
    }

    @Test
    void shouldListFlightsByAirline(){

    }

    @Test
    void shouldListScheduledFlights(){

    }
}
