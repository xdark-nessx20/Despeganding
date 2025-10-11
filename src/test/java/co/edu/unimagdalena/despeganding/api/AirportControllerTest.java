package co.edu.unimagdalena.despeganding.api;

import co.edu.unimagdalena.despeganding.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.edu.unimagdalena.despeganding.api.dto.AirportDTOs.*;
import co.edu.unimagdalena.despeganding.services.AirportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AirportController.class)
public class AirportControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    AirportService service;

    @Test
    void createAirport_shouldReturn201AndLocation() throws Exception {
        var req = new AirportCreateRequest("JFK", "John F Kennedy", "New York");
        var resp = new AirportResponse(1L, "JFK", "John F Kennedy", "New York");

        when(service.createAirport(any())).thenReturn(resp);

        mvc.perform(post("/api/v1/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/v1/airports/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("JFK"))
                .andExpect(jsonPath("$.city").value("New York"));
    }

    @Test
    void createAirport_shouldReturn400WhenCodeInvalid() throws Exception {
        var req = new AirportCreateRequest("JF", "JFK", "New York");

        mvc.perform(post("/api/v1/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAirport_shouldReturn200() throws Exception {
        when(service.getAirport(1L)).thenReturn(new AirportResponse(1L, "JFK", "John F Kennedy", "New York"));

        mvc.perform(get("/api/v1/airports/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("JFK"));
    }

    @Test
    void getAirport_shouldReturn404WhenNotFound() throws Exception {
        when(service.getAirport(99L)).thenThrow(new NotFoundException("Airport 99 not found"));

        mvc.perform(get("/api/v1/airports/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Airport 99 not found"));
    }

    @Test
    void getAirportByCode_shouldReturn200() throws Exception {
        when(service.getAirportByCode("JFK")).thenReturn(new AirportResponse(1L, "JFK", "John F Kennedy", "New York"));

        mvc.perform(get("/api/v1/airports/by-code")
                        .param("code", "JFK"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("JFK"));
    }

    @Test
    void getCityAirports_shouldReturn200WithList() throws Exception {
        List<AirportResponse> airports = Arrays.asList(
                new AirportResponse(1L, "JFK", "John F Kennedy", "New York"),
                new AirportResponse(2L, "LGA", "LaGuardia", "New York")
        );
        when(service.getCityAirports("New York")).thenReturn(airports);

        mvc.perform(get("/api/v1/airports/by-city")
                        .param("city", "New York"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].city").value("New York"));
    }

    @Test
    void updateAirport_shouldReturn200() throws Exception {
        var req = new AirportUpdateRequest("JFK", "JFK International");
        var resp = new AirportResponse(1L, "JFK", "JFK International", "New York");

        when(service.updateAirport(eq(1L), any())).thenReturn(resp);

        mvc.perform(patch("/api/v1/airports/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("JFK International"));
    }

    @Test
    void deleteAirport_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/v1/airports/1"))
                .andExpect(status().isNoContent());

        verify(service).deleteAirport(1L);
    }

    @Test
    void listAllAirports_shouldReturn200WithPage() throws Exception {
        Page<AirportResponse> page = new PageImpl<>(List.of(
                new AirportResponse(1L, "JFK", "John F Kennedy", "New York")
        ));

        when(service.listAllAirports(any(PageRequest.class))).thenReturn(page);

        mvc.perform(get("/api/v1/airports")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

}
