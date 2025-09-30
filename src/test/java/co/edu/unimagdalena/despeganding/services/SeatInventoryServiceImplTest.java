package co.edu.unimagdalena.despeganding.services;

import co.edu.unimagdalena.despeganding.api.dto.SeatInventoryDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.Cabin;
import co.edu.unimagdalena.despeganding.domain.entities.Flight;
import co.edu.unimagdalena.despeganding.domain.entities.SeatInventory;
import co.edu.unimagdalena.despeganding.domain.repositories.FlightRepository;
import co.edu.unimagdalena.despeganding.domain.repositories.SeatInventoryRepository;
import co.edu.unimagdalena.despeganding.services.impl.SeatInventoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SeatInventoryServiceImplTest {
    @Mock FlightRepository flightRepository;
    @Mock SeatInventoryRepository seatInventoryRepository;

    @InjectMocks SeatInventoryServiceImpl seatInventoryService;

    @Test
    void shouldCreateSeatInventoryAndMapToResponse(){
        var now = OffsetDateTime.now();
        when(flightRepository.findById(1L)).thenReturn(Optional.of(Flight.builder().id(1L).number("XD0001")
                .departureTime(now).arrivalTime(now.plusHours(4)).build()));
        when(seatInventoryRepository.save(any())).thenAnswer(invocation -> {
            SeatInventory sI = invocation.getArgument(0);
            sI.setId(10L);
            return sI;
        });

        var response = seatInventoryService.createSeatInventory(1L,
                new SeatInventoryCreateRequest("ECONOMY", 40, 35));

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.flight_id()).isEqualTo(1L);
        assertThat(response.cabin()).isEqualTo("ECONOMY");
    }

    @Test
    void shouldUpdateSeatInventoryAndMapToResponse(){
        when(seatInventoryRepository.findById(10L)).thenReturn(Optional.of(
                SeatInventory.builder().id(10L).cabin(Cabin.valueOf("ECONOMY")).totalSeats(45)
                .availableSeats(20).build()
        ));
        when(seatInventoryRepository.save(any())).thenAnswer(invocation -> invocation.<SeatInventory>getArgument(0));

        var response = seatInventoryService.updateSeatInventory(10L,
                new SeatInventoryUpdateRequest(null, 40, 5));

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.cabin()).isEqualTo("ECONOMY");
        assertThat(response.totalSeats()).isEqualTo(40);
        assertThat(response.availableSeats()).isEqualTo(5);
    }

    @Test
    void shouldListSeatInventoriesByFlight(){
        var now = OffsetDateTime.now();
        var flight = Optional.of(Flight.builder().id(1L).number("XD0001").departureTime(now)
                .arrivalTime(now.plusHours(4)).build());

        when(flightRepository.findById(1L)).thenReturn(flight);
        when(seatInventoryRepository.findByFlight_Id(1L)).thenReturn(List.of(
                SeatInventory.builder().id(10L).cabin(Cabin.valueOf("ECONOMY")).totalSeats(50)
                        .availableSeats(10).flight(flight.get()).build(),
                SeatInventory.builder().id(11L).cabin(Cabin.valueOf("PREMIUM")).totalSeats(40)
                        .availableSeats(15).flight(flight.get()).build(),
                SeatInventory.builder().id(12L).cabin(Cabin.valueOf("BUSINESS")).totalSeats(30)
                        .availableSeats(20).flight(flight.get()).build()
        ));

        var response = seatInventoryService.listSeatInventoriesByFlight(1L);

        assertThat(response).hasSize(3);
        assertThat(response).extracting(SeatInventoryResponse::flight_id)
                        .allMatch(flight_id -> flight_id.equals(1L));
    }

    @Test
    void shouldGetSeatInventoryByFlightAndCabin(){
        var now = OffsetDateTime.now();
        var flight = Optional.of(Flight.builder().id(1L).number("XD0001").departureTime(now)
                .arrivalTime(now.plusHours(4)).build());
        var flight_2 = Optional.of(Flight.builder().id(2L).number("XD0002").departureTime(now.plusHours(1))
                .arrivalTime(now.plusHours(3)).build());
        var seats = Optional.of(SeatInventory.builder().id(10L).cabin(Cabin.valueOf("ECONOMY")).totalSeats(50)
                .availableSeats(10).flight(flight.get()).build());
        var seats_2 = Optional.of(SeatInventory.builder().id(12L).cabin(Cabin.valueOf("BUSINESS")).totalSeats(30)
                .availableSeats(7).flight(flight_2.get()).build());

        when(flightRepository.findById(2L)).thenReturn(flight_2);
        when(seatInventoryRepository.findByFlight_IdAndCabin(2L, Cabin.valueOf("BUSINESS")))
                .thenReturn(seats_2);

        var response = seatInventoryService.getSeatInventoryByFlightAndCabin(2L, "BUSINESS");

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(12L);
        assertThat(response.flight_id()).isEqualTo(2L);
        assertThat(response.cabin()).isEqualTo("BUSINESS");
    }
}
