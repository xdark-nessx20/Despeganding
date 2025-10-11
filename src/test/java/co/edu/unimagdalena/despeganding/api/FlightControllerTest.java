package co.edu.unimagdalena.despeganding.api;

import co.edu.unimagdalena.despeganding.api.dto.FlightDTOs.*;
import co.edu.unimagdalena.despeganding.exceptions.NotFoundException;
import co.edu.unimagdalena.despeganding.services.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlightController.class)
public class FlightControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    FlightService flightService;

    @Test
    void create_ShouldReturn201AndLocation() throws Exception {
        var now = OffsetDateTime.now();
        var x = now.plusHours(1);
        var y = now.plusHours(4);
        var request = new FlightCreateRequest("XD1", x, y, 1L,
                101L, 102L);

        var response = new FlightResponse(1001L, "XD1", x, y, 1L, "RandomAirline",
                101L, "XDAirport", 102L, "DXAirport",
                Set.of());

        when(flightService.createFlight(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/flights").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/v1/flights/1001")))
                .andExpect(jsonPath("$.id").value(1001L));
    }

    @Test
    void get_ShouldReturn404() throws Exception {
        when(flightService.getFlight(99L)).thenThrow(new NotFoundException("Room 99 not found"));

        mockMvc.perform(get("/api/v1/flights/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Room 99 not found"));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/v1/flights/8"))
                .andExpect(status().isNoContent());
        verify(flightService).deleteFlight(8L);
    }

    @Test
    void getByAirline_ShouldReturn200() throws Exception {
        var page = new PageImpl<>(List.of(
                new FlightResponse(1001L, "XD1", OffsetDateTime.now(), OffsetDateTime.now().plusHours(5), 1L, "ZZZZZ",
                        101L, "XDAirport", 102L, "DXAirport",
                        Set.of()))
        );

        when(flightService.listFlightsByAirline("ZZZZZ", PageRequest.of(0, 5))).thenReturn(page);

        mockMvc.perform(get("/api/v1/flight?airlineName=ZZZZZ&page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}
