package co.edu.unimagdalena.despeganding.api;

import co.edu.unimagdalena.despeganding.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.edu.unimagdalena.despeganding.api.dto.AirlineDTOs.*;
import co.edu.unimagdalena.despeganding.services.AirlineService;
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

@WebMvcTest(AirlineController.class)
public class AirlineControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    AirlineService service;

    @Test
    void createAirline_shouldReturn201AndLocation() throws Exception {
        var req = new AirlineCreateRequest("AA", "American Airlines");
        var resp = new AirlineResponse(1L, "AA", "American Airlines");

        when(service.createAirline(any())).thenReturn(resp);

        mvc.perform(post("/api/v1/airlines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/v1/airlines/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("AA"))
                .andExpect(jsonPath("$.name").value("American Airlines"));
    }

    @Test
    void createAirline_shouldReturn400WhenCodeInvalid() throws Exception {
        var req = new AirlineCreateRequest("A", "American Airlines");

        mvc.perform(post("/api/v1/airlines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAirline_shouldReturn200() throws Exception {
        when(service.getAirline(1L)).thenReturn(new AirlineResponse(1L, "AA", "American Airlines"));

        mvc.perform(get("/api/v1/airlines/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("AA"));
    }

    @Test
    void getAirline_shouldReturn404WhenNotFound() throws Exception {
        when(service.getAirline(99L)).thenThrow(new NotFoundException("Airline 99 not found"));

        mvc.perform(get("/api/v1/airlines/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Airline 99 not found"));
    }

    @Test
    void getAirlineByCode_shouldReturn200() throws Exception {
        when(service.getAirlineByCode("AA")).thenReturn(new AirlineResponse(1L, "AA", "American Airlines"));

        mvc.perform(get("/api/v1/airlines/by-code")
                        .param("code", "AA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("AA"));
    }

    @Test
    void updateAirline_shouldReturn200() throws Exception {
        var req = new AirlineUpdateRequest("AA", "American Airlines Updated");
        var resp = new AirlineResponse(1L, "AA", "American Airlines Updated");

        when(service.updateAirline(eq(1L), any())).thenReturn(resp);

        mvc.perform(patch("/api/v1/airlines/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("American Airlines Updated"));
    }

    @Test
    void deleteAirline_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/v1/airlines/1"))
                .andExpect(status().isNoContent());

        verify(service).deleteAirline(1L);
    }

    @Test
    void listAllAirlines_shouldReturn200WithList() throws Exception {
        List<AirlineResponse> airlines = Arrays.asList(
                new AirlineResponse(1L, "AA", "American Airlines"),
                new AirlineResponse(2L, "DL", "Delta Airlines")
        );
        when(service.listAllAirlines()).thenReturn(airlines);

        mvc.perform(get("/api/v1/airlines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].code").value("AA"))
                .andExpect(jsonPath("$[1].code").value("DL"));
    }

    @Test
    void listAllAirlinesPage_shouldReturn200WithPage() throws Exception {
        Page<AirlineResponse> page = new PageImpl<>(List.of(
                new AirlineResponse(1L, "AA", "American Airlines")
        ));

        when(service.listAllAirlinesPage(any(PageRequest.class))).thenReturn(page);

        mvc.perform(get("/api/v1/airlines")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }


}
