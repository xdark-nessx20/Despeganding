package co.edu.unimagdalena.despeganding.api;

import co.edu.unimagdalena.despeganding.api.dto.SeatInventoryDTOs.*;
import co.edu.unimagdalena.despeganding.services.SeatInventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeatInventoryController.class)
public class SeatInventoryControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;
    @MockitoBean
    SeatInventoryService seatService;

    @Test
    void create_ShouldReturn201() throws Exception {
        var request = new SeatInventoryCreateRequest("ECONOMY", 50, 25);
        var response = new SeatInventoryResponse(100001L, "ECONOMY", 50, 25, 1001L, "XD1");

        when(seatService.createSeatInventory(eq(1001L), request)).thenReturn(response);

        mvc.perform(post("/api/v1/flights/1001/seatInventories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.endsWith("/api/v1/flights/1001/seatInventories/100001")))
                .andExpect(jsonPath("$.id").value(100001));

    }

    @Test
    void delete_shouldReturn204() throws Exception{
        mvc.perform(delete("/api/v1/flights/1001/seatInventories/5"))
                .andExpect(status().isNoContent());
        verify(seatService).deleteSeatInventory(5L);
    }

    @Test
    void listByFlight_ShouldReturn200() throws Exception{
        when(seatService.listSeatInventoriesByFlight(eq(1001L))).thenReturn(List.of(
            new SeatInventoryResponse(100001L, "ECONOMY", 50, 25, 1001L, "XD1"),
            new SeatInventoryResponse(100002L, "PREMIUM", 50, 1, 1001L, "XD1")
        ));

        mvc.perform(get("/api/v1/flights/1001/seatInventories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100001))
                .andExpect(jsonPath("$[1].id").value(100002));
    }
}
