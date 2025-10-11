package co.edu.unimagdalena.despeganding.api;

import co.edu.unimagdalena.despeganding.api.dto.BookingDTOs.*;
import co.edu.unimagdalena.despeganding.services.BookingItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingItemController.class)
public class BookingItemControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;
    @MockitoBean
    BookingItemService bookingItemService;

    @Test
    void add_shouldReturn201AndLocation() throws Exception {
        var request = new BookingItemCreateRequest("ECONOMY", new BigDecimal(100000), 1, 1001L);
        var response = new BookingItemResponse(777L, "ECONOMY", new BigDecimal(100000), 1, 11111L, 1001L, "XD1");

        when(bookingItemService.addBookingItem(eq(11111L), any())).thenReturn(response);

        mvc.perform(post("/api/v1/bookings/11111/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/v1/bookings/11111/items/777")))
                .andExpect(jsonPath("$.id").value(777));
    }

    @Test
    void listByBooking_shouldReturn200() throws Exception {
        var response = List.of(new BookingItemResponse(777L, "ECONOMY", new BigDecimal(100000), 1, 11111L, 1001L, "XD1"));
        when(bookingItemService.listBookingItemsByBooking(11111L)).thenReturn(response);

        mvc.perform(get("/api/v1/bookings/11111/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].booking_id").value(11111));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/v1/bookings/11111/items/5"))
                .andExpect(status().isNoContent());
        verify(bookingItemService).deleteBookingItem(5L);
    }
}
