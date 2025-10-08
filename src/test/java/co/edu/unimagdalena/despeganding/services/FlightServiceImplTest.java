package co.edu.unimagdalena.despeganding.services;

import co.edu.unimagdalena.despeganding.api.dto.FlightDTOs.*;
import co.edu.unimagdalena.despeganding.api.dto.TagDTOs.TagResponse;
import co.edu.unimagdalena.despeganding.domain.entities.Airline;
import co.edu.unimagdalena.despeganding.domain.entities.Airport;
import co.edu.unimagdalena.despeganding.domain.entities.Flight;
import co.edu.unimagdalena.despeganding.domain.entities.Tag;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.*;

import java.time.OffsetDateTime;
import java.util.List;
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
        var now = OffsetDateTime.now();
        var destination = Optional.of(Airport.builder().id(1L).code("XD").name("Zzzairport").city("Soledad").build());
        var destination_2 = Optional.of(Airport.builder().id(2L).code("DX").name("Derivairport").city("Malambo").build());

        when(airportRepository.findById(2L)).thenReturn(destination_2);
        when(flightRepository.findById(101L)).thenReturn(Optional.of(Flight.builder().id(101L).number("XD0001").departureTime(now).arrivalTime(now.plusHours(7))
                .destination(destination.get()).build()));
        when(flightRepository.save(any())).thenAnswer(invocation -> invocation.<Flight>getArgument(0));

        var response = flightService.updateFlight(new FlightUpdateRequest("XD0002", null, null), 101L, 2L);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(101L);
        assertThat(response.number()).isEqualTo("XD0002");
        assertThat(response.destination_airport_id()).isEqualTo(2L);
    }

    @Test
    void shouldAddTagToFlightAndMapToResponse() {
        var now = OffsetDateTime.now();
        var flight = Optional.of(Flight.builder().id(1L).number("XD0001").departureTime(now).arrivalTime(now.plusHours(7)).build());
        when(flightRepository.findById(1L)).thenReturn(flight);
        when(tagRepository.findById(10001L)).thenReturn(Optional.of(Tag.builder().id(10001L).name("tag 1").build()));

        var response = flightService.addTagToFlight(1L, 10001L);

        assertThat(response.tags()).hasSize(1);
        assertThat(response.tags()).extracting(TagResponse::name).containsExactly("tag 1");

    }

    @Test
    void shouldListFlightsByAirline(){
        var airline = Optional.of(Airline.builder().id(1L).code("XD").name("DownAirline").build());
        when(airlineRepository.findById(1L)).thenReturn(airline);
        when(flightRepository.findByAirline_Name("DownAirline", Pageable.unpaged())).thenReturn(new PageImpl<>(List.of(
                Flight.builder().id(101L).number("XD0001").airline(airline.get()).build(),
                Flight.builder().id(102L).number("XD0002").airline(airline.get()).build(),
                Flight.builder().id(103L).number("XD0003").airline(airline.get()).build()
        )));

        var response = flightService.listFlightsByAirline(1L, Pageable.unpaged());

        assertThat(response).hasSize(3);
        assertThat(response).extracting(FlightResponse::airline_id).allMatch(airline_id -> airline_id.equals(1L));
    }

    @Test
    void shouldListScheduledFlights(){
        var origin = Optional.of(Airport.builder().id(1L).code("XD").name("Origin").build());
        var destination = Optional.of(Airport.builder().id(2L).code("DX").name("Final Destination").build());
        var destination_2 = Optional.of(Airport.builder().id(3L).code("ZY").name("Final Destination 2").build());

        var now = OffsetDateTime.now(); var dep_from_time_1 = now.minusHours(12); var dep_to_time_1 = now.plusHours(12);
        var dep_from_time_2 = now.plusHours(2); var dep_to_time_2 = now.plusDays(1).plusHours(12);

        var f1 = Flight.builder().id(101L).origin(origin.get()).destination(destination.get()).departureTime(now).arrivalTime(now.plusHours(5)).build();
        var f2 = Flight.builder().id(102L).origin(origin.get()).destination(destination.get()).departureTime(now.plusHours(4)).arrivalTime(now.plusDays(1)).build();
        var f3 = Flight.builder().id(103L).origin(origin.get()).destination(destination_2.get()).departureTime(now.plusDays(1))
                .arrivalTime(now.plusDays(2).plusHours(12)).build();

        when(airportRepository.findById(1L)).thenReturn(origin);
        when(airportRepository.findById(2L)).thenReturn(destination);
        when(flightRepository.findByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween(origin.get().getCode(), destination.get().getCode(),
                dep_from_time_1, dep_to_time_1, Pageable.unpaged())).thenReturn(new PageImpl<>(List.of(f1, f2)));
        when(flightRepository.filterByOriginAndDestinationOptionalAndDepartureTimeBetween(origin.get().getCode(), null, dep_from_time_2,
                dep_to_time_2)).thenReturn(List.of(f2, f3));

        var response_1 = flightService.listScheduledFlights(1L, 2L, dep_from_time_1, dep_to_time_1, Pageable.unpaged());
        var response_2 = flightService.listScheduledFlights(1L, null, dep_from_time_2, dep_to_time_2, Pageable.unpaged());

        //Response 1
        assertThat(response_1).hasSize(2);
        assertThat(response_1).extracting(FlightResponse::origin_airport_id).containsExactly(1L, 1L);
        assertThat(response_1).extracting(FlightResponse::destination_airport_id).containsExactly(2L, 2L);
        assertThat(response_1).allSatisfy(flight -> assertThat(flight.departureTime()).isBetween(dep_from_time_1, dep_to_time_1));

        //Response 2
        assertThat(response_2).hasSize(2);
        assertThat(response_2).extracting(FlightResponse::origin_airport_id).containsExactly(1L, 1L);
        assertThat(response_2).extracting(FlightResponse::departureTime).allMatch(
                dep_time -> dep_time.isAfter(dep_from_time_2) && dep_time.isBefore(dep_to_time_2)
        );
    }
}
