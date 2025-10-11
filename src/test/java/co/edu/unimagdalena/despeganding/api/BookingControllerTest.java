package co.edu.unimagdalena.despeganding.api;

import co.edu.unimagdalena.despeganding.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.edu.unimagdalena.despeganding.api.dto.BookingDTOs.*;
import co.edu.unimagdalena.despeganding.services.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    BookingService service;

    @Test
    void createBooking_shouldReturn201AndLocation() throws Exception {
        var req = new BookingCreateRequest(1L);
        var item = new BookingItemResponse(1L, "Economy", new BigDecimal("250.00"), 1, 1L, 101L, "AA123");
        var resp = new BookingResponse(1L, OffsetDateTime.now(), "John Doe", "john@example.com",
                Collections.singletonList(item));

        when(service.createBooking(any())).thenReturn(resp);

        mvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/v1/bookings/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.passenger_name").value("John Doe"));
    }

    @Test
    void createBooking_shouldReturn400WhenPassengerIdNull() throws Exception {
        var req = new BookingCreateRequest(null);

        mvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBooking_shouldReturn200() throws Exception {
        var resp = new BookingResponse(1L, OffsetDateTime.now(), "John Doe", "john@example.com",
                Collections.emptyList());
        when(service.getBooking(1L)).thenReturn(resp);

        mvc.perform(get("/api/v1/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.passenger_name").value("John Doe"));
    }

    @Test
    void getBooking_shouldReturn404WhenNotFound() throws Exception {
        when(service.getBooking(99L)).thenThrow(new NotFoundException("Booking 99 not found"));

        mvc.perform(get("/api/v1/bookings/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Booking 99 not found"));
    }

    @Test
    void listBookingsBetweenDates_shouldReturn200WithList() throws Exception {
        OffsetDateTime start = OffsetDateTime.now().minusDays(7);
        OffsetDateTime end = OffsetDateTime.now();

        var resp = new BookingResponse(1L, OffsetDateTime.now(), "John Doe", "john@example.com",
                Collections.emptyList());
        List<BookingResponse> bookings = List.of(resp);

        when(service.listBookingsBetweenDates(any(OffsetDateTime.class), any(OffsetDateTime.class)))
                .thenReturn(bookings);

        mvc.perform(get("/api/v1/bookings")
                        .param("start", start.toString())
                        .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void listBookingsByPassengerEmail_shouldReturn200WithPage() throws Exception {
        var resp = new BookingResponse(1L, OffsetDateTime.now(), "John Doe", "john@example.com",
                Collections.emptyList());
        Page<BookingResponse> page = new PageImpl<>(List.of(resp));

        when(service.listBookingsByPassengerEmailAndOrderedMostRecently(anyString(), any(Pageable.class)))
                .thenReturn(page);

        mvc.perform(get("/api/v1/bookings")
                        .param("email", "john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getBookingWithAllDetails_shouldReturn200() throws Exception {
        var item = new BookingItemResponse(1L, "Economy", new BigDecimal("250.00"), 1, 1L, 101L, "AA123");
        var resp = new BookingResponse(1L, OffsetDateTime.now(), "John Doe", "john@example.com",
                Collections.singletonList(item));

        when(service.getBookingWithAllDetails(1L)).thenReturn(resp);

        mvc.perform(get("/api/v1/bookings/details/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].cabin").value("Economy"));
    }

    @Test
    void updateBooking_shouldReturn200() throws Exception {
        var resp = new BookingResponse(1L, OffsetDateTime.now(), "Jane Doe", "jane@example.com",
                Collections.emptyList());

        when(service.updateBooking(eq(1L), eq(2L))).thenReturn(resp);

        mvc.perform(patch("/api/v1/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteBooking_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/v1/bookings/1"))
                .andExpect(status().isNoContent());

        verify(service).deleteBooking(1L);
    }

}
