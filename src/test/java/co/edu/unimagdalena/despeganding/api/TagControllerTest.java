package co.edu.unimagdalena.despeganding.api;

import co.edu.unimagdalena.despeganding.api.dto.TagDTOs.*;
import co.edu.unimagdalena.despeganding.services.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TagController.class)
public class TagControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    TagService tagService;

    @Test
    void create_ShouldReturn201AndLocation() throws Exception {
        var request = new TagCreateRequest("pruebi-tag");
        var response = new TagResponse(12345678L, "pruebi-tag");

        when(tagService.createTag(request)).thenReturn(response);
        mockMvc.perform(post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/v1/tags/12345678")))
                .andExpect(jsonPath("$.id").value(12345678));
    }

    @Test
    void delete_shouldReturn204() throws Exception{
        mockMvc.perform(delete("/api/v1/tags/12345678"))
                .andExpect(status().isNoContent());
        verify(tagService).deleteTag(12345678L);
    }
}
