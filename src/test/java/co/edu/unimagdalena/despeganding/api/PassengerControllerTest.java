package co.edu.unimagdalena.despeganding.api;

import co.edu.unimagdalena.despeganding.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.edu.unimagdalena.despeganding.api.dto.PassengerDTOs.*;
import co.edu.unimagdalena.despeganding.services.PassengerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PassengerController.class)
public class PassengerControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    PassengerService service;

    @Test
    void createPassenger_shouldReturn201AndLocation() throws Exception {
        var profile = new PassengerProfileDto("1234567890", "57");
        var req = new PassengerCreateRequest("John Doe", "john@example.com", profile);
        var resp = new PassengerResponse(1L, "John Doe", "john@example.com", profile);

        when(service.createPassenger(any())).thenReturn(resp);

        mvc.perform(post("/api/v1/passengers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/v1/passengers/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.profile.phone").value("1234567890"));
    }
    @Test
    void getPassenger_shouldReturn200() throws Exception {
        when(service.getPassenger(1L)).thenReturn(new PassengerResponse(1L, "John Doe", "john@example.com", null));

        mvc.perform(get("/api/v1/passengers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    void getPassenger_shouldReturn404WhenNotFound() throws Exception {
        when(service.getPassenger(99L)).thenThrow(new NotFoundException("Passenger 99 not found"));

        mvc.perform(get("/api/v1/passengers/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Passenger 99 not found"));
    }

    @Test
    void deletePassenger_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/v1/passengers/1"))
                .andExpect(status().isNoContent());

        verify(service).deletePassenger(1L);
    }

    @Test
    void getPassengerByEmail_shouldReturn200() throws Exception {
        var profile = new PassengerProfileDto("1234567890", "57");
        when(service.getPassengerByEmail("john@example.com"))
                .thenReturn(new PassengerResponse(1L, "John Doe", "john@example.com", profile));

        mvc.perform(get("/api/v1/passengers")
                        .param("email", "john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void getPassengerWithProfile_shouldReturn200() throws Exception {
        var profile = new PassengerProfileDto("1234567890", "57");
        when(service.getPassengerWithProfile("john@example.com"))
                .thenReturn(new PassengerResponse(1L, "John Doe", "john@example.com", profile));

        mvc.perform(get("/api/v1/passengers/by-email-with-profile")
                        .param("email", "john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.profile").exists())
                .andExpect(jsonPath("$.profile.phone").value("1234567890"));
    }

    @Test
    void updatePassenger_shouldReturn200() throws Exception {
        var profile = new PassengerProfileDto("1234567890", "57");
        var req = new PassengerUpdateRequest("John Doe Updated", "john.updated@example.com", profile);
        var resp = new PassengerResponse(1L, "John Doe Updated", "john.updated@example.com", profile);

        when(service.updatePassenger(eq(1L), any())).thenReturn(resp);

        mvc.perform(patch("/api/v1/passengers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"));
    }


    @Test
    void listAllPassengers_shouldReturn200WithPage() throws Exception {
        var profile = new PassengerProfileDto("1234567890", "57");
        Page<PassengerResponse> page = new PageImpl<>(List.of(
                new PassengerResponse(1L, "John Doe", "john@example.com", profile)
        ));

        when(service.listAllPassengers(any(PageRequest.class))).thenReturn(page);

        mvc.perform(get("/api/v1/passengers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void createPassenger_shouldReturn400WhenEmailInvalid() throws Exception {
        var profile = new PassengerProfileDto("1234567890", "57");
        var req = new PassengerCreateRequest("John Doe", "invalid-email", profile);

        mvc.perform(post("/api/v1/passengers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPassenger_shouldReturn400WhenNameBlank() throws Exception {
        var profile = new PassengerProfileDto("1234567890", "57");
        var req = new PassengerCreateRequest("", "john@example.com", profile);

        mvc.perform(post("/api/v1/passengers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

}
